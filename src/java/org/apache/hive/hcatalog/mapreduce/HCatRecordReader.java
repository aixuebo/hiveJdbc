/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.hive.hcatalog.mapreduce;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.metadata.HiveStorageHandler;
import org.apache.hadoop.hive.serde2.Deserializer;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hive.hcatalog.common.HCatConstants;
import org.apache.hive.hcatalog.common.HCatUtil;
import org.apache.hive.hcatalog.data.DefaultHCatRecord;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.LazyHCatRecord;
import org.apache.hive.hcatalog.data.schema.HCatSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The HCat wrapper for the underlying RecordReader,
 * this ensures that the initialize on
 * the underlying record reader is done with the underlying split,
 * not with HCatSplit.
 */
class HCatRecordReader extends RecordReader<WritableComparable, HCatRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(HCatRecordReader.class);

  private InputErrorTracker errorTracker;

  WritableComparable currentKey;//获取hadoop里面存储的原始key
  Writable currentValue;//获取hadoop里面存储的原始value
  HCatRecord currentHCatRecord;//当前转换后的一行数据

  /** The underlying record reader to delegate to. */
  private org.apache.hadoop.mapred.RecordReader<WritableComparable, Writable> baseRecordReader;

  /** The storage handler used */
  private final HiveStorageHandler storageHandler;//如何对hive表进行序列化与反序列化

  private Deserializer deserializer;//如何对原始的value进行反序列化成hive的数据类型

  private Map<String, Object> valuesNotInDataCols;//对输出的内容中,如果需要分区字段,则设置分区字段的值,key是需要的输出字段,value是默认的值,即分区字段的值

  private HCatSchema outputSchema = null;//输出的字段
  private HCatSchema dataSchema = null;//数据表里面的字段

  /**
   * Instantiates a new hcat record reader.
   */
  public HCatRecordReader(HiveStorageHandler storageHandler,
              Map<String, Object> valuesNotInDataCols) {
    this.storageHandler = storageHandler;
    this.valuesNotInDataCols = valuesNotInDataCols;
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapreduce.RecordReader#initialize(
   * org.apache.hadoop.mapreduce.InputSplit,
   * org.apache.hadoop.mapreduce.TaskAttemptContext)
   */
  @Override
  public void initialize(org.apache.hadoop.mapreduce.InputSplit split,
               TaskAttemptContext taskContext) throws IOException, InterruptedException {

    HCatSplit hcatSplit = InternalUtil.castToHCatSplit(split);//对应的split数据块

    baseRecordReader = createBaseRecordReader(hcatSplit, storageHandler, taskContext);
    createDeserializer(hcatSplit, storageHandler, taskContext);

    // Pull the output schema out of the TaskAttemptContext
    outputSchema = (HCatSchema) HCatUtil.deserialize(
      taskContext.getConfiguration().get(HCatConstants.HCAT_KEY_OUTPUT_SCHEMA));

    if (outputSchema == null) {
      outputSchema = hcatSplit.getTableSchema();//输出需要的字段
    }

    // Pull the table schema out of the Split info
    // TODO This should be passed in the TaskAttemptContext instead
    dataSchema = hcatSplit.getDataSchema();

    errorTracker = new InputErrorTracker(taskContext.getConfiguration());
  }

  private org.apache.hadoop.mapred.RecordReader createBaseRecordReader(HCatSplit hcatSplit,
                                     HiveStorageHandler storageHandler, TaskAttemptContext taskContext) throws IOException {

    JobConf jobConf = HCatUtil.getJobConfFromContext(taskContext);
    HCatUtil.copyJobPropertiesToJobConf(hcatSplit.getPartitionInfo().getJobProperties(), jobConf);
    org.apache.hadoop.mapred.InputFormat inputFormat =
      HCatInputFormat.getMapRedInputFormat(jobConf, storageHandler.getInputFormatClass());//获取读取该数据块的读取方式
    return inputFormat.getRecordReader(hcatSplit.getBaseSplit(), jobConf,
      InternalUtil.createReporter(taskContext));//打开该数据块
  }

  //创建反序列化对象
  private void createDeserializer(HCatSplit hcatSplit, HiveStorageHandler storageHandler,
                  TaskAttemptContext taskContext) throws IOException {

    deserializer = ReflectionUtils.newInstance(storageHandler.getSerDeClass(),
      taskContext.getConfiguration());//如何序列化与反序列化

    try {
      InternalUtil.initializeDeserializer(deserializer, storageHandler.getConf(),
        hcatSplit.getPartitionInfo().getTableInfo(),
        hcatSplit.getPartitionInfo().getPartitionSchema());
    } catch (SerDeException e) {
      throw new IOException("Failed initializing deserializer "
        + storageHandler.getSerDeClass().getName(), e);
    }
  }

  /* (non-Javadoc)
  * @see org.apache.hadoop.mapreduce.RecordReader#getCurrentKey()
  */
  @Override
  public WritableComparable getCurrentKey()
    throws IOException, InterruptedException {
    return currentKey;
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapreduce.RecordReader#getCurrentValue()
   */
  @Override
  public HCatRecord getCurrentValue() throws IOException, InterruptedException {
    return currentHCatRecord;
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.mapreduce.RecordReader#getProgress()
   */
  @Override
  public float getProgress() {
    try {
      return baseRecordReader.getProgress();
    } catch (IOException e) {
      LOG.warn("Exception in HCatRecord reader", e);
    }
    return 0.0f; // errored
  }

  /**
   * Check if the wrapped RecordReader has another record, and if so convert it into an
   * HCatRecord. We both check for records and convert here so a configurable percent of
   * bad records can be tolerated.
   *
   * @return if there is a next record
   * @throws IOException on error
   * @throws InterruptedException on error
   */
  @Override
  public boolean nextKeyValue() throws IOException, InterruptedException {
    if (currentKey == null) {
      currentKey = baseRecordReader.createKey();
      currentValue = baseRecordReader.createValue();
    }

    while (baseRecordReader.next(currentKey, currentValue)) {
      HCatRecord r = null;
      Throwable t = null;

      errorTracker.incRecords();

      try {
        Object o = deserializer.deserialize(currentValue);//反序列化成对象
        r = new LazyHCatRecord(o, deserializer.getObjectInspector());//转换成一行数据
      } catch (Throwable throwable) {
        t = throwable;
      }

      if (r == null) {
        errorTracker.incErrors(t);
        continue;
      }

      DefaultHCatRecord dr = new DefaultHCatRecord(outputSchema.size());
      int i = 0;
      for (String fieldName : outputSchema.getFieldNames()) {//输出列
        if (dataSchema.getPosition(fieldName) != null) {//数据列
          dr.set(i, r.get(fieldName, dataSchema));
        } else {
          dr.set(i, valuesNotInDataCols.get(fieldName));//设置该列的值
        }
        i++;
      }

      currentHCatRecord = dr;
      return true;
    }

    return false;
  }

  /* (non-Javadoc)
  * @see org.apache.hadoop.mapreduce.RecordReader#close()
  */
  @Override
  public void close() throws IOException {
    baseRecordReader.close();
  }

  /**
   * Tracks number of of errors in input and throws a Runtime exception
   * if the rate of errors crosses a limit.
   * <br/>
   * The intention is to skip over very rare file corruption or incorrect
   * input, but catch programmer errors (incorrect format, or incorrect
   * deserializers etc).
   *
   * This class was largely copied from Elephant-Bird (thanks @rangadi!)
   * https://github.com/kevinweil/elephant-bird/blob/master/core/src/main/java/com/twitter/elephantbird/mapreduce/input/LzoRecordReader.java
   */
  static class InputErrorTracker {
    long numRecords;
    long numErrors;

    double errorThreshold; // max fraction of errors allowed
    long minErrors; // throw error only after this many errors

    InputErrorTracker(Configuration conf) {
      errorThreshold = conf.getFloat(HCatConstants.HCAT_INPUT_BAD_RECORD_THRESHOLD_KEY,
        HCatConstants.HCAT_INPUT_BAD_RECORD_THRESHOLD_DEFAULT);
      minErrors = conf.getLong(HCatConstants.HCAT_INPUT_BAD_RECORD_MIN_KEY,
        HCatConstants.HCAT_INPUT_BAD_RECORD_MIN_DEFAULT);
      numRecords = 0;
      numErrors = 0;
    }

    void incRecords() {
      numRecords++;
    }

    void incErrors(Throwable cause) {
      numErrors++;
      if (numErrors > numRecords) {
        // incorrect use of this class
        throw new RuntimeException("Forgot to invoke incRecords()?");
      }

      if (cause == null) {
        cause = new Exception("Unknown error");
      }

      if (errorThreshold <= 0) { // no errors are tolerated
        throw new RuntimeException("error while reading input records", cause);
      }

      LOG.warn("Error while reading an input record ("
        + numErrors + " out of " + numRecords + " so far ): ", cause);

      double errRate = numErrors / (double) numRecords;

      // will always excuse the first error. We can decide if single
      // error crosses threshold inside close() if we want to.
      if (numErrors >= minErrors && errRate > errorThreshold) {
        LOG.error(numErrors + " out of " + numRecords
          + " crosses configured threshold (" + errorThreshold + ")");
        throw new RuntimeException("error rate while reading input records crossed threshold", cause);
      }
    }
  }
}
