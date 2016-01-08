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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ql.plan;

import java.util.List;
import java.util.Map;

/**
 * Truncates managed table or partition
 * 截断表内数据
 * 从表或者表分区删除所有行，不指定分区，将截断表中的所有分区，也可以一次指定多个分区，截断多个分区。
 * TRUNCATE TABLE tableName [PARTITION (name=value,name=value,name)] [COLUMNS (column1,column2...)]
 */
@Explain(displayName = "Truncate Table or Partition")
public class TruncateTableDesc extends DDLDesc {

  private static final long serialVersionUID = 1L;

  private String tableName;//针对哪个表
  private Map<String, String> partSpec;//针对哪些partition
  private List<Integer> columnIndexes;//COLUMNS (column1,column2...) 语法中的属性在table中的属性序号一一对应关系
  private String inputDir;//设置该partition的老路径,即为截断前的数据路径
  private String outputDir;
  private ListBucketingCtx lbCtx;

  public TruncateTableDesc() {
  }

  public TruncateTableDesc(String tableName, Map<String, String> partSpec) {
    this.tableName = tableName;
    this.partSpec = partSpec;
  }

  @Explain(displayName = "TableName")
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  @Explain(displayName = "Partition Spec")
  public Map<String, String> getPartSpec() {
    return partSpec;
  }

  public void setPartSpec(Map<String, String> partSpec) {
    this.partSpec = partSpec;
  }

  @Explain(displayName = "Column Indexes")
  public List<Integer> getColumnIndexes() {
    return columnIndexes;
  }

  public void setColumnIndexes(List<Integer> columnIndexes) {
    this.columnIndexes = columnIndexes;
  }

  public String getInputDir() {
    return inputDir;
  }

  public void setInputDir(String inputDir) {
    this.inputDir = inputDir;
  }

  public String getOutputDir() {
    return outputDir;
  }

  public void setOutputDir(String outputDir) {
    this.outputDir = outputDir;
  }

  public ListBucketingCtx getLbCtx() {
    return lbCtx;
  }

  public void setLbCtx(ListBucketingCtx lbCtx) {
    this.lbCtx = lbCtx;
  }
}
