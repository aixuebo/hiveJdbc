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
1.indexPropertiesList ��ʽ: key=value,key=value
2.indexProperties ��ʽ (key=value,key=value)
3.indexPropertiesPrefixed ��ʽ IDXPROPERTIES (key=value,key=value)
4.indexTblName ��ʽ: IN TABLE tableName
5.autoRebuild �����Ŀ�ѡ��  ��ʽ  WITH DEFERRED REBUILD
6.indexComment �����ı�ע  ��ʽ COMMENT String
7.createIndexStatement ����:��table01��������,���������column1,column2�����н�������,����������table01_index
��ʽ:
CREATE INDEX "��������" ON TABLE "����" (column1,column2) AS "COMPACT��aggregate��bitmap������classȫ·��,�μ�HiveIndex��"
[WITH DEFERRED REBUILD ��ʾ���ڽ�������] 
[IDXPROPERTIES (key=value,key=value) ��ʾ��index�Ķ���������Ϣ]
[IN TABLE tableName]
[tableRowFormat]
[tableFileFormat]
[LOCATION xxx ��ʾ�洢��HDFS�ϵ�·��]
[tablePropertiesPrefixed]
[COMMENT String �����ı�ע] 
 */
public class CreateIndexDesc extends DDLDesc implements Serializable {

  private static final long serialVersionUID = 1L;
  String tableName;//���� ON TABLE "����"
  String indexName;//����INDEX "��������"
  List<String> indexedCols;//����(column1,column2),����Щ�н�������
  String indexTableName;//����IN TABLE tableName
  boolean deferredRebuild;//����WITH DEFERRED REBUILD
  String inputFormat;
  String outputFormat;
  String serde;
  String storageHandler;
  String indexTypeHandlerClass;//����AS 'COMPACT',����������,HiveIndex���ڵ�name�����Զ����classȫ·��
  String location;//LOCATION xxx ��ʾ�洢��HDFS�ϵ�·��
  Map<String, String> idxProps;//������ȫ������,IDXPROPERTIES (key=value,key=value) ��ʾ��index�Ķ���������Ϣ
  Map<String, String> tblProps;//���ȫ������
  Map<String, String> serdeProps;//���л���ʽ,����proto,json�ȸ�ʽ��Ҫ������������Ϣ
  
  //����tableRowFormat,��һ�еĲ������
  String collItemDelim;//����֮��Ĳ��
  String fieldDelim;//����֮��Ĳ��
  String fieldEscape;//�ָ�����ʱ���õ�ת���ַ�
  String lineDelim;//��֮��Ĳ��
  String mapKeyDelim;//map֮��Ĳ��

  String indexComment;//����COMMENT String �����ı�ע

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
