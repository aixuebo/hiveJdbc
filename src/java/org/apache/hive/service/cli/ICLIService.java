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
package org.apache.hive.service.cli;

import java.util.List;
import java.util.Map;




public interface ICLIService {
  //创建一个session
  public abstract SessionHandle openSession(String username, String password,
      Map<String, String> configuration)
      throws HiveSQLException;

  //创建一个session
  public abstract SessionHandle openSessionWithImpersonation(String username, String password,
      Map<String, String> configuration, String delegationToken)
      throws HiveSQLException;

  //关闭一个session
  public abstract void closeSession(SessionHandle sessionHandle)
      throws HiveSQLException;

  //获取该session下的一个信息
  public abstract GetInfoValue getInfo(SessionHandle sessionHandle, GetInfoType infoType)
      throws HiveSQLException;

  //执行该session下的一个动作操作
  public abstract OperationHandle executeStatement(SessionHandle sessionHandle, String statement,
      Map<String, String> confOverlay)
      throws HiveSQLException;

  //异步执行该session下的一个动作操作
  public abstract OperationHandle executeStatementAsync(SessionHandle sessionHandle,
      String statement, Map<String, String> confOverlay)
      throws HiveSQLException;

  //获取该session下的一个列的类型集合
  public abstract OperationHandle getTypeInfo(SessionHandle sessionHandle)
      throws HiveSQLException;

  //获取该session下的一个数据库顶层catalogs信息
  public abstract OperationHandle getCatalogs(SessionHandle sessionHandle)
      throws HiveSQLException;

  //获取该session下的匹配数的据库集合信息
  public abstract OperationHandle getSchemas(SessionHandle sessionHandle,
      String catalogName, String schemaName)
      throws HiveSQLException;

  //获取该session下的匹配数据库下的所有匹配表集合信息
  public abstract OperationHandle getTables(SessionHandle sessionHandle,
      String catalogName, String schemaName, String tableName, List<String> tableTypes)
      throws HiveSQLException;

  public abstract OperationHandle getTableTypes(SessionHandle sessionHandle)
      throws HiveSQLException;

  public abstract OperationHandle getColumns(SessionHandle sessionHandle,
      String catalogName, String schemaName, String tableName, String columnName)
      throws HiveSQLException;

  public abstract OperationHandle getFunctions(SessionHandle sessionHandle,
      String catalogName, String schemaName, String functionName)
      throws HiveSQLException;

  public abstract OperationState getOperationStatus(OperationHandle opHandle)
      throws HiveSQLException;

  public abstract void cancelOperation(OperationHandle opHandle)
      throws HiveSQLException;

  public abstract void closeOperation(OperationHandle opHandle)
      throws HiveSQLException;

  public abstract TableSchema getResultSetMetadata(OperationHandle opHandle)
      throws HiveSQLException;

  public abstract RowSet fetchResults(OperationHandle opHandle, FetchOrientation orientation,
      long maxRows)
      throws HiveSQLException;

  public abstract RowSet fetchResults(OperationHandle opHandle)
      throws HiveSQLException;

}
