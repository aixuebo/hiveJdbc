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
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.metadata.HiveStorageHandler;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hive.hcatalog.common.ErrorType;
import org.apache.hive.hcatalog.common.HCatConstants;
import org.apache.hive.hcatalog.common.HCatException;
import org.apache.hive.hcatalog.common.HCatUtil;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.schema.HCatSchema;

public abstract class HCatBaseOutputFormat extends OutputFormat<WritableComparable<?>, HCatRecord> {

//  static final private Log LOG = LogFactory.getLog(HCatBaseOutputFormat.class);

  /**
   * Gets the table schema for the table specified in the HCatOutputFormat.setOutput call
   * on the specified job context.
   * @param conf the Configuration object
   * @return the table schema
   * @throws IOException if HCatOutputFormat.setOutput has not been called for the passed context
   * 还原输出需要的列信息
   */
  public static HCatSchema getTableSchema(Configuration conf) throws IOException {
    OutputJobInfo jobInfo = getJobInfo(conf);
    return jobInfo.getTableInfo().getDataColumns();
  }

  /**
   * Check for validity of the output-specification for the job.
   * @param context information about the job
   * @throws IOException when output should not be attempted
   */
  @Override
  public void checkOutputSpecs(JobContext context
  ) throws IOException, InterruptedException {
    getOutputFormat(context).checkOutputSpecs(context);
  }

  /**
   * Gets the output format instance.
   * @param context the job context
   * @return the output format instance
   * @throws IOException
   */
  protected OutputFormat<WritableComparable<?>, HCatRecord> getOutputFormat(JobContext context) 
    throws IOException {
    OutputJobInfo jobInfo = getJobInfo(context.getConfiguration());
    HiveStorageHandler storageHandler = HCatUtil.getStorageHandler(context.getConfiguration(), 
        jobInfo.getTableInfo().getStorerInfo());
    // Always configure storage handler with jobproperties/jobconf before calling any methods on it
    configureOutputStorageHandler(context);
    if (storageHandler instanceof FosterStorageHandler) {
      return new FileOutputFormatContainer(ReflectionUtils.newInstance(
          storageHandler.getOutputFormatClass(),context.getConfiguration()));
    }
    else { 
      return new DefaultOutputFormatContainer(ReflectionUtils.newInstance(
          storageHandler.getOutputFormatClass(),context.getConfiguration()));
    }
  }

  /**
   * Gets the HCatOuputJobInfo object by reading the Configuration and deserializing
   * the string. If InputJobInfo is not present in the configuration, throws an
   * exception since that means HCatOutputFormat.setOutput has not been called.
   * @param conf the job Configuration object
   * @return the OutputJobInfo object
   * @throws IOException the IO exception
   */
  public static OutputJobInfo getJobInfo(Configuration conf) throws IOException {
    String jobString = conf.get(HCatConstants.HCAT_KEY_OUTPUT_INFO);
    if (jobString == null) {
      throw new HCatException(ErrorType.ERROR_NOT_INITIALIZED);
    }

    return (OutputJobInfo) HCatUtil.deserialize(jobString);
  }

  /**
   * Configure the output storage handler
   * @param jobContext the job context
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  static void configureOutputStorageHandler(
    JobContext jobContext) throws IOException {
    configureOutputStorageHandler(jobContext, (List<String>) null);
  }

  /**
   * Configure the output storage handler with allowing specification of missing dynamic partvals
   * @param jobContext the job context
   * @param dynamicPartVals 动态分区的具体的值
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  static void configureOutputStorageHandler(
    JobContext jobContext, List<String> dynamicPartVals) throws IOException {
    Configuration conf = jobContext.getConfiguration();
    try {
      OutputJobInfo jobInfo = (OutputJobInfo) HCatUtil.deserialize(conf.get(HCatConstants.HCAT_KEY_OUTPUT_INFO));
      HiveStorageHandler storageHandler = HCatUtil.getStorageHandler(jobContext.getConfiguration(),jobInfo.getTableInfo().getStorerInfo());

      Map<String, String> partitionValues = jobInfo.getPartitionValues();//设置分区的值
      String location = jobInfo.getLocation();

      if (dynamicPartVals != null) {//说明设置了动态分区
        // dynamic part vals specified
        List<String> dynamicPartKeys = jobInfo.getDynamicPartitioningKeys();//获取动态分区列
        if (dynamicPartVals.size() != dynamicPartKeys.size()) {
          throw new HCatException(ErrorType.ERROR_INVALID_PARTITION_VALUES,
            "Unable to configure dynamic partitioning for storage handler, mismatch between"
              + " number of partition values obtained[" + dynamicPartVals.size()
              + "] and number of partition values required[" + dynamicPartKeys.size() + "]");
        }
        for (int i = 0; i < dynamicPartKeys.size(); i++) {
          partitionValues.put(dynamicPartKeys.get(i), dynamicPartVals.get(i));//设置动态分区的值,变成静态分区
        }

//            // re-home location, now that we know the rest of the partvals
//            Table table = jobInfo.getTableInfo().getTable();
//
//            List<String> partitionCols = new ArrayList<String>();
//            for(FieldSchema schema : table.getPartitionKeys()) {
//              partitionCols.add(schema.getName());
//            }
        jobInfo.setPartitionValues(partitionValues);
      }

      HCatUtil.configureOutputStorageHandler(storageHandler, conf, jobInfo);
    } catch (Exception e) {
      if (e instanceof HCatException) {
        throw (HCatException) e;
      } else {
        throw new HCatException(ErrorType.ERROR_INIT_STORAGE_HANDLER, e);
      }
    }
  }

  /**
   * Configure the output storage handler, with allowing specification
   * of partvals from which it picks the dynamic partvals
   * @param context the job context
   * @param jobInfo the output job info
   * @param fullPartSpec
   * @throws IOException
   */

  protected static void configureOutputStorageHandler(
    JobContext context, OutputJobInfo jobInfo,
    Map<String, String> fullPartSpec) throws IOException {
    List<String> dynamicPartKeys = jobInfo.getDynamicPartitioningKeys();
    if ((dynamicPartKeys == null) || (dynamicPartKeys.isEmpty())) {
      configureOutputStorageHandler(context, (List<String>) null);
    } else {
      List<String> dynKeyVals = new ArrayList<String>();
      for (String dynamicPartKey : dynamicPartKeys) {
        dynKeyVals.add(fullPartSpec.get(dynamicPartKey));
      }
      configureOutputStorageHandler(context, dynKeyVals);
    }
  }

    /**
     * 设置自定义输出schema的时候,调用该方法
     * @param jobInfo
     * @param schema     自定义输出的列集合
     * @param partMap  自定义的分区字段
     * @throws HCatException
     * @throws IOException
     */
  protected static void setPartDetails(OutputJobInfo jobInfo, final HCatSchema schema,
                     Map<String, String> partMap) throws HCatException, IOException {
    List<Integer> posOfPartCols = new ArrayList<Integer>();
    List<Integer> posOfDynPartCols = new ArrayList<Integer>();

    // If partition columns occur in data, we want to remove them.
    // So, find out positions of partition columns in schema provided by user.
    // We also need to update the output Schema with these deletions.

    // Note that, output storage handlers never sees partition columns in data
    // or schema.

    HCatSchema schemaWithoutParts = new HCatSchema(schema.getFields());//设置的全部列---最终是没有分区的列集合
    for (String partKey : partMap.keySet()) {
      Integer idx;
      if ((idx = schema.getPosition(partKey)) != null) {//说明列中有分区字段
        posOfPartCols.add(idx);//设置分区字段在schema中的列的序号
        schemaWithoutParts.remove(schema.get(partKey));//移除该分区列
      }
    }

    // Also, if dynamic partitioning is being used, we want to
    // set appropriate list of columns for the columns to be dynamically specified.
    // These would be partition keys too, so would also need to be removed from
    // output schema and partcols

    if (jobInfo.isDynamicPartitioningUsed()) {
      for (String partKey : jobInfo.getDynamicPartitioningKeys()) {//动态分区列
        Integer idx;
        if ((idx = schema.getPosition(partKey)) != null) {
          posOfPartCols.add(idx);//是分区列
          posOfDynPartCols.add(idx);//是动态分区列
          schemaWithoutParts.remove(schema.get(partKey));//移除该列
        }
      }
    }

    HCatUtil.validatePartitionSchema(
      new Table(jobInfo.getTableInfo().getTable()), schemaWithoutParts);
    jobInfo.setPosOfPartCols(posOfPartCols);
    jobInfo.setPosOfDynPartCols(posOfDynPartCols);
    jobInfo.setOutputSchema(schemaWithoutParts);
  }
}
