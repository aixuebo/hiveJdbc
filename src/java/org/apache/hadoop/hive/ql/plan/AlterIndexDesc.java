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
import java.util.Map;

/**
 * AlterIndexDesc.
 * 修改index的属性信息 "indexName" ON "tableName" [PARTITION (name=value,name=value,name)] SET IDXPROPERTIES (key=value,key=value),该属于ADDPROPS
 * "indexName" ON "tableName" [PARTITION (name=value,name=value,name)] REBUILD
 */
@Explain(displayName = "Alter Index")
public class AlterIndexDesc extends DDLDesc implements Serializable {
  private static final long serialVersionUID = 1L;
  private String indexName;//索引名称
  private String baseTable;//该索引所属的表
  private String dbName;//该表所属数据库
  private Map<String, String> partSpec; // partition specification of partitions touched 要修改哪个分区的索引属性信息
  private Map<String, String> props;//所有最终修改的属性集合信息

  /**
   * alterIndexTypes.
   *
   */
  public static enum AlterIndexTypes {
    UPDATETIMESTAMP,//更新时间戳,即执行"indexName" ON "tableName" [PARTITION (name=value,name=value,name)] REBUILD,即修改index的属性信息
    ADDPROPS//更新索引的属性信息
    };

  AlterIndexTypes op;

  public AlterIndexDesc() {
  }

  public AlterIndexDesc(AlterIndexTypes type) {
    this.op = type;
  }

  /**
   * @return the name of the index
   */
  @Explain(displayName = "name")
  public String getIndexName() {
    return indexName;
  }

  /**
   * @param indexName
   *          the indexName to set
   */
  public void setIndexName(String indexName) {
    this.indexName = indexName;
  }

  /**
   * @return the baseTable
   */
  @Explain(displayName = "new name")
  public String getBaseTableName() {
    return baseTable;
  }

  /**
   * @param baseTable
   *          the baseTable to set
   */
  public void setBaseTableName(String baseTable) {
    this.baseTable = baseTable;
  }

  /**
   * @return the partition spec
   */
  public Map<String, String> getSpec() {
    return partSpec;
  }

  /**
   * @param partSpec
   *          the partition spec to set
   */
  public void setSpec(Map<String, String> partSpec) {
    this.partSpec = partSpec;
  }

  /**
   * @return the name of the database that the base table is in
   */
  public String getDbName() {
    return dbName;
  }

  /**
   * @param dbName
   *          the dbName to set
   */
  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  /**
   * @return the op
   */
  public AlterIndexTypes getOp() {
    return op;
  }

  /**
   * @param op
   *          the op to set
   */
  public void setOp(AlterIndexTypes op) {
    this.op = op;
  }

  /**
   * @return the props
   */
  @Explain(displayName = "properties")
  public Map<String, String> getProps() {
    return props;
  }

  /**
   * @param props
   *          the props to set
   */
  public void setProps(Map<String, String> props) {
    this.props = props;
  }
}
