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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * create index descriptor
1.indexPropertiesList 格式: key=value,key=value
2.indexProperties 格式 (key=value,key=value)
3.indexPropertiesPrefixed 格式 IDXPROPERTIES (key=value,key=value)
4.indexTblName 格式: IN TABLE tableName
5.autoRebuild 索引的可选项  格式  WITH DEFERRED REBUILD
6.indexComment 索引的备注  格式 COMMENT String
7.createIndexStatement 含义:对table01表建立索引,该索引针对column1,column2两个列建立索引,索引名称是table01_index
格式:
CREATE INDEX "索引名称" ON TABLE "表名" (column1,column2) AS "COMPACT、aggregate、bitmap、或者class全路径,参见HiveIndex类"
[WITH DEFERRED REBUILD 表示延期建立索引] 
[IDXPROPERTIES (key=value,key=value) 表示该index的额外属性信息]
[IN TABLE tableName]
[tableRowFormat]
[tableFileFormat]
[LOCATION xxx 表示存储在HDFS上的路径]
[tablePropertiesPrefixed]
[COMMENT String 索引的备注] 
 */
public class CreateIndexDesc extends DDLDesc implements Serializable {

  private static final long serialVersionUID = 1L;
  String tableName;//解析 ON TABLE "表名"
  String indexName;//解析INDEX "索引名称"
  List<String> indexedCols;//解析(column1,column2),对哪些列建立索引
  String indexTableName;//解析IN TABLE tableName
  boolean deferredRebuild;//解析WITH DEFERRED REBUILD
  String inputFormat;
  String outputFormat;
  String serde;
  String storageHandler;
  String indexTypeHandlerClass;//解析AS 'COMPACT',索引的引擎,HiveIndex表内的name或者自定义的class全路径
  String location;//LOCATION xxx 表示存储在HDFS上的路径
  Map<String, String> idxProps;//索引的全局属性,IDXPROPERTIES (key=value,key=value) 表示该index的额外属性信息
  Map<String, String> tblProps;//表的全局属性
  Map<String, String> serdeProps;//序列化格式,比如proto,json等格式需要的属性配置信息
  
  //解析tableRowFormat,即一行的拆分条件
  String collItemDelim;//集合之间的拆分
  String fieldDelim;//属性之间的拆分
  String fieldEscape;//分隔属性时候用的转义字符
  String lineDelim;//行之间的拆分
  String mapKeyDelim;//map之间的拆分

  String indexComment;//解析COMMENT String 索引的备注

  public CreateIndexDesc() {
    super();
  }

  public CreateIndexDesc(String tableName, String indexName,
      List<String> indexedCols, String indexTableName, boolean deferredRebuild,
      String inputFormat, String outputFormat, String storageHandler,
      String typeName, String location, Map<String, String> idxProps, Map<String, String> tblProps,
      String serde, Map<String, String> serdeProps, String collItemDelim,
      String fieldDelim, String fieldEscape, String lineDelim,
      String mapKeyDelim, String indexComment) {
    super();
    this.tableName = tableName;
    this.indexName = indexName;
    this.indexedCols = indexedCols;
    this.indexTableName = indexTableName;
    this.deferredRebuild = deferredRebuild;
    this.inputFormat = inputFormat;
    this.outputFormat = outputFormat;
    this.serde = serde;
    this.storageHandler = storageHandler;
    this.indexTypeHandlerClass = typeName;
    this.location = location;
    this.idxProps = idxProps;
    this.tblProps = tblProps;
    this.serde = serde;
    this.serdeProps = serdeProps;
    this.collItemDelim = collItemDelim;
    this.fieldDelim = fieldDelim;
    this.fieldEscape = fieldEscape;
    this.lineDelim = lineDelim;
    this.mapKeyDelim = mapKeyDelim;
    this.indexComment = indexComment;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getIndexName() {
    return indexName;
  }

  public void setIndexName(String indexName) {
    this.indexName = indexName;
  }

  public List<String> getIndexedCols() {
    return indexedCols;
  }

  public void setIndexedCols(List<String> indexedCols) {
    this.indexedCols = indexedCols;
  }

  public String getIndexTableName() {
    return indexTableName;
  }

  public void setIndexTableName(String indexTableName) {
    this.indexTableName = indexTableName;
  }

  public boolean isDeferredRebuild() {
    return deferredRebuild;
  }

  public boolean getDeferredRebuild() {
    return deferredRebuild;
  }

  public void setDeferredRebuild(boolean deferredRebuild) {
    this.deferredRebuild = deferredRebuild;
  }

  public String getInputFormat() {
    return inputFormat;
  }

  public void setInputFormat(String inputFormat) {
    this.inputFormat = inputFormat;
  }

  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
  }

  public String getSerde() {
    return serde;
  }

  public void setSerde(String serde) {
    this.serde = serde;
  }

  public String getStorageHandler() {
    return storageHandler;
  }

  public void setStorageHandler(String storageHandler) {
    this.storageHandler = storageHandler;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Map<String, String> getIdxProps() {
    return idxProps;
  }

  public void setIdxProps(Map<String, String> idxProps) {
    this.idxProps = idxProps;
  }

  public Map<String, String> getTblProps() {
    return tblProps;
  }

  public void setTblProps(Map<String, String> tblProps) {
    this.tblProps = tblProps;
  }

  public Map<String, String> getSerdeProps() {
    return serdeProps;
  }

  public void setSerdeProps(Map<String, String> serdeProps) {
    this.serdeProps = serdeProps;
  }

  public String getCollItemDelim() {
    return collItemDelim;
  }

  public void setCollItemDelim(String collItemDelim) {
    this.collItemDelim = collItemDelim;
  }

  public String getFieldDelim() {
    return fieldDelim;
  }

  public void setFieldDelim(String fieldDelim) {
    this.fieldDelim = fieldDelim;
  }

  public String getFieldEscape() {
    return fieldEscape;
  }

  public void setFieldEscape(String fieldEscape) {
    this.fieldEscape = fieldEscape;
  }

  public String getLineDelim() {
    return lineDelim;
  }

  public void setLineDelim(String lineDelim) {
    this.lineDelim = lineDelim;
  }

  public String getMapKeyDelim() {
    return mapKeyDelim;
  }

  public void setMapKeyDelim(String mapKeyDelim) {
    this.mapKeyDelim = mapKeyDelim;
  }

  public String getIndexTypeHandlerClass() {
    return indexTypeHandlerClass;
  }

  public void setIndexTypeHandlerClass(String indexTypeHandlerClass) {
    this.indexTypeHandlerClass = indexTypeHandlerClass;
  }

  public String getIndexComment() {
    return indexComment;
  }

  public void setIndexComment(String indexComment) {
    this.indexComment = indexComment;
  }

}
