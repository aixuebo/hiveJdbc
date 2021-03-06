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

package org.apache.hive.service.cli.operation;

import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hive.service.cli.FetchOrientation;
import org.apache.hive.service.cli.HiveSQLException;
import org.apache.hive.service.cli.OperationState;
import org.apache.hive.service.cli.OperationType;
import org.apache.hive.service.cli.RowSet;
import org.apache.hive.service.cli.TableSchema;
import org.apache.hive.service.cli.session.HiveSession;

/**
 * GetSchemasOperation.
 * 获取匹配的数据库集合
 */
public class GetSchemasOperation extends MetadataOperation {
  private final String catalogName;//数据库表达式
  private final String schemaName;

  //返回时候定义的对象
  private static final TableSchema RESULT_SET_SCHEMA = new TableSchema()
  .addStringColumn("TABLE_SCHEM", "Schema name.")
  .addStringColumn("TABLE_CATALOG", "Catalog name.");

  private RowSet rowSet;//具体的返回值

  protected GetSchemasOperation(HiveSession parentSession,
      String catalogName, String schemaName) {
    super(parentSession, OperationType.GET_SCHEMAS);
    this.catalogName = catalogName;
    this.schemaName = schemaName;
  }

  /* (non-Javadoc)
   * @see org.apache.hive.service.cli.Operation#run()
   */
  @Override
  public void run() throws HiveSQLException {
    setState(OperationState.RUNNING);
    rowSet = new RowSet();
    try {
      IMetaStoreClient metastoreClient = getParentSession().getMetaStoreClient();
      String schemaPattern = convertSchemaPattern(schemaName);
      for (String dbName : metastoreClient.getDatabases(schemaPattern)) {//获取匹配的数据库集合
        rowSet.addRow(RESULT_SET_SCHEMA, new Object[] {dbName, DEFAULT_HIVE_CATALOG});
      }
      setState(OperationState.FINISHED);
    } catch (Exception e) {
      setState(OperationState.ERROR);
      throw new HiveSQLException(e);
    }
  }


  /* (non-Javadoc)
   * @see org.apache.hive.service.cli.Operation#getResultSetSchema()
   */
  @Override
  public TableSchema getResultSetSchema() throws HiveSQLException {
    assertState(OperationState.FINISHED);
    return RESULT_SET_SCHEMA;
  }

  /* (non-Javadoc)
   * @see org.apache.hive.service.cli.Operation#getNextRowSet(org.apache.hive.service.cli.FetchOrientation, long)
   */
  @Override
  public RowSet getNextRowSet(FetchOrientation orientation, long maxRows) throws HiveSQLException {
    assertState(OperationState.FINISHED);
    return rowSet.extractSubset((int)maxRows);
  }
}
