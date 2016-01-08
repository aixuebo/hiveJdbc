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
import java.util.ArrayList;
import java.util.List;

/**
 * DropTableDesc.ɾ��һ�������
 * DROP TABLE [IF EXISTS] tableName
 * String DROP [IF Exists] PARTITION(key ���� value,key ���� value),PARTITION( key ���� value,key ���� value) [IGNORE PROTECTION]
 */
@Explain(displayName = "Drop Table")
public class DropTableDesc extends DDLDesc implements Serializable {
  private static final long serialVersionUID = 1L;

  String tableName;//ɾ���ı���
  ArrayList<PartitionSpec> partSpecs;//ɾ����Щ��������,ÿһ��PartitionSpec��ʾһ������
  boolean expectView;//true��ʾ�ñ���һ����ͼ��,������ʵ���
  boolean ifExists;
  
  /**
   * true��ʾIGNORE PROTECTION��������,�����𵽱�������,Ĭ����false
   * ��partition����ɾ��,����ignoreProtection=false,�����쳣,˵���÷���������ɾ��,�����ֹͣ����
   */
  boolean ignoreProtection;
  
  //У���Ƿ����е�partition���Զ���String���͵�,false��ʾ�в���String���͵�,true��ʾ���еĶ���String���͵�partiton����
  //ɾ��ĳһ�����ĳЩ������ʱ��,����������������Ͳ���String��,ֻ�������=�Ų���ɾ��,��������зǵȺŵĲ���,����>=�Ȳ���
  boolean stringPartitionColumns; // This is due to JDO not working very well with
                                  // non-string partition columns.
                                  // We need a different codepath for them

  public DropTableDesc() {
  }

  /**
   * @param tableName
   */
  public DropTableDesc(String tableName, boolean expectView,
                       boolean ifExists, boolean stringPartitionColumns) {
    this.tableName = tableName;
    partSpecs = null;
    this.expectView = expectView;
    this.ifExists = ifExists;
    this.ignoreProtection = false;
    this.stringPartitionColumns = stringPartitionColumns;
  }

  public DropTableDesc(String tableName, List<PartitionSpec> partSpecs,
                       boolean expectView, boolean stringPartitionColumns,
                       boolean ignoreProtection) {

    this.tableName = tableName;
    this.partSpecs = new ArrayList<PartitionSpec>(partSpecs.size());
    for (int i = 0; i < partSpecs.size(); i++) {
      this.partSpecs.add(partSpecs.get(i));
    }
    this.ignoreProtection = ignoreProtection;
    this.expectView = expectView;
    this.stringPartitionColumns = stringPartitionColumns;
  }

  /**
   * @return the tableName
   */
  @Explain(displayName = "table")
  public String getTableName() {
    return tableName;
  }

  /**
   * @param tableName
   *          the tableName to set
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * @return the partSpecs
   */
  public ArrayList<PartitionSpec> getPartSpecs() {
    return partSpecs;
  }

  /**
   * @param partSpecs
   *          the partSpecs to set
   */
  public void setPartSpecs(ArrayList<PartitionSpec> partSpecs) {
    this.partSpecs = partSpecs;
  }

  /**
   * @return whether or not protection will be ignored for the partition
   */
  public boolean getIgnoreProtection() {
    return ignoreProtection;
  }

  /**
   * @param ignoreProtection
   *          set whether or not protection will be ignored for the partition
   */
   public void setIgnoreProtection(boolean ignoreProtection) {
     this.ignoreProtection = ignoreProtection;
   }

  /**
   * @return whether to expect a view being dropped
   */
  public boolean getExpectView() {
    return expectView;
  }

  /**
   * @param expectView
   *          set whether to expect a view being dropped
   */
  public void setExpectView(boolean expectView) {
    this.expectView = expectView;
  }

  /**
   * @return whether IF EXISTS was specified
   */
  public boolean getIfExists() {
    return ifExists;
  }

  /**
   * @param ifExists
   *          set whether IF EXISTS was specified
   */
  public void setIfExists(boolean ifExists) {
    this.ifExists = ifExists;
  }

  public boolean isStringPartitionColumns() {
    return stringPartitionColumns;
  }

  public void setStringPartitionColumns(boolean stringPartitionColumns) {
    this.stringPartitionColumns = stringPartitionColumns;
  }
}
