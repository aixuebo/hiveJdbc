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

package org.apache.hadoop.hive.jdbc;

import java.sql.SQLException;

/**
 * Table metadata.
 * 表示查询回来的table的元数据信息
 */
public class JdbcTable {
  private String tableCatalog;
  private String tableName;//表名字
  private String type;//表类型,内部表 还是外部表 还是视图
  private String comment;//表备注

  public JdbcTable(String tableCatalog, String tableName, String type, String comment) {
    this.tableCatalog = tableCatalog;
    this.tableName = tableName;
    this.type = type;
    this.comment = comment;
  }

  public String getTableCatalog() {
    return tableCatalog;
  }

  public String getTableName() {
    return tableName;
  }

  public String getType() {
    return type;
  }

  public String getSqlTableType() throws SQLException {
    return HiveDatabaseMetaData.toJdbcTableType(type);
  }

  public String getComment() {
    return comment;
  }
}
