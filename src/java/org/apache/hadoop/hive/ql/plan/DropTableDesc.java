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
 * DropTableDesc.删除一个表操作
 * DROP TABLE [IF EXISTS] tableName
 * String DROP [IF Exists] PARTITION(key 符号 value,key 符号 value),PARTITION( key 符号 value,key 符号 value) [IGNORE PROTECTION]
 */
@Explain(displayName = "Drop Table")
public class DropTableDesc extends DDLDesc implements Serializable {
  private static final long serialVersionUID = 1L;

  String tableName;//删除的表名
  ArrayList<PartitionSpec> partSpecs;//删除那些分区集合,每一个PartitionSpec表示一个分区
  boolean expectView;//true表示该表是一个视图表,而不是实体表
  boolean ifExists;
  
  /**
   * true表示IGNORE PROTECTION被设置了,不用起到保护作用,默认是false
   * 当partition不可删除,并且ignoreProtection=false,则抛异常,说明该分区不允许删除,则程序停止进行
   */
  boolean ignoreProtection;
  
  //校验是否所有的partition属性都是String类型的,false表示有不是String类型的,true表示所有的都是String类型的partiton名称
  //删除某一个表的某些分区的时候,如果分区的属性类型不是String的,只允许进行=号操作删除,不允许进行非等号的操作,例如>=等操作
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
