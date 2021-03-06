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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.metadata.HiveStorageHandler;
import org.apache.hadoop.hive.serde2.SerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hive.hcatalog.common.ErrorType;
import org.apache.hive.hcatalog.common.HCatException;
import org.apache.hive.hcatalog.common.HCatUtil;
import org.apache.hive.hcatalog.data.HCatRecord;

/**
 * Part of the FileOutput*Container classes See {@link FileOutputFormatContainer} for more
 * information
 * 将数据写入到hive中---有分区信息
 */
abstract class FileRecordWriterContainer extends RecordWriterContainer {

  protected final HiveStorageHandler storageHandler;
  protected final SerDe serDe;//如何序列化要写入的数据
  protected final ObjectInspector objectInspector;//要写入的数据类型,其中包含了分区列,该分区列可能是动态分区,也可能是静态分区

  private final List<Integer> partColsToDel;//分区列集合以及该分区列在schema中的序号

  protected OutputJobInfo jobInfo;
  protected TaskAttemptContext context;

  /**
   * @param baseWriter RecordWriter to contain
   * @param context current TaskAttemptContext
   * @throws IOException
   * @throws InterruptedException
   */
  public FileRecordWriterContainer(
      RecordWriter<? super WritableComparable<?>, ? super Writable> baseWriter,
      TaskAttemptContext context) throws IOException, InterruptedException {
    super(context, baseWriter);
    this.context = context;
    jobInfo = HCatOutputFormat.getJobInfo(context.getConfiguration());

    storageHandler =
        HCatUtil.getStorageHandler(context.getConfiguration(), jobInfo.getTableInfo()
            .getStorerInfo());
    serDe = ReflectionUtils.newInstance(storageHandler.getSerDeClass(), context.getConfiguration());
    objectInspector = InternalUtil.createStructObjectInspector(jobInfo.getOutputSchema());
    try {
      InternalUtil.initializeOutputSerDe(serDe, context.getConfiguration(), jobInfo);
    } catch (SerDeException e) {
      throw new IOException("Failed to inialize SerDe", e);
    }

    // If partition columns occur in data, we want to remove them.
    partColsToDel = jobInfo.getPosOfPartCols();
    if (partColsToDel == null) {
      throw new HCatException("It seems that setSchema() is not called on "
          + "HCatOutputFormat. Please make sure that method is called.");
    }
  }

  /**
   * @return the storagehandler
   */
  public HiveStorageHandler getStorageHandler() {
    return storageHandler;
  }

  abstract protected LocalFileWriter getLocalFileWriter(HCatRecord value) throws IOException,
      HCatException;

  @Override
  public void write(WritableComparable<?> key, HCatRecord value) throws IOException,
      InterruptedException {
    LocalFileWriter localFileWriter = getLocalFileWriter(value);
    RecordWriter localWriter = localFileWriter.getLocalWriter();
    ObjectInspector localObjectInspector = localFileWriter.getLocalObjectInspector();
    SerDe localSerDe = localFileWriter.getLocalSerDe();
    OutputJobInfo localJobInfo = localFileWriter.getLocalJobInfo();

    for (Integer colToDel : partColsToDel) {//移除分区字段对应的值
      value.remove(colToDel);
    }

    // The key given by user is ignored
    try {
      localWriter.write(NullWritable.get(),
          localSerDe.serialize(value.getAll(), localObjectInspector));//将数据序列化成hive需要的字节,存储到文件中
    } catch (SerDeException e) {
      throw new IOException("Failed to serialize object", e);
    }
  }

  class LocalFileWriter {
    private RecordWriter localWriter;
    private ObjectInspector localObjectInspector;
    private SerDe localSerDe;
    private OutputJobInfo localJobInfo;

    public LocalFileWriter(RecordWriter localWriter, ObjectInspector localObjectInspector,
        SerDe localSerDe, OutputJobInfo localJobInfo) {
      this.localWriter = localWriter;
      this.localObjectInspector = localObjectInspector;
      this.localSerDe = localSerDe;
      this.localJobInfo = localJobInfo;
    }

    public RecordWriter getLocalWriter() {
      return localWriter;
    }

    public ObjectInspector getLocalObjectInspector() {
      return localObjectInspector;
    }

    public SerDe getLocalSerDe() {
      return localSerDe;
    }

    public OutputJobInfo getLocalJobInfo() {
      return localJobInfo;
    }
  }
}
