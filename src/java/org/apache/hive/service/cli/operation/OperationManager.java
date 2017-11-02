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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hive.service.AbstractService;
import org.apache.hive.service.cli.FetchOrientation;
import org.apache.hive.service.cli.HiveSQLException;
import org.apache.hive.service.cli.OperationHandle;
import org.apache.hive.service.cli.OperationState;
import org.apache.hive.service.cli.RowSet;
import org.apache.hive.service.cli.TableSchema;
import org.apache.hive.service.cli.session.HiveSession;

/**
 * OperationManager.
 * 一个服务,用于在服务端不断的提供操作的服务入口
 */
public class OperationManager extends AbstractService {

  private HiveConf hiveConf;
  //持有所有的操作集合,每一个操作有一个主键OperationHandle用于识别
  private final Map<OperationHandle, Operation> handleToOperation = new HashMap<OperationHandle, Operation>();


  public OperationManager() {
    super("OperationManager");
  }

  @Override
  public synchronized void init(HiveConf hiveConf) {
    this.hiveConf = hiveConf;

    super.init(hiveConf);
  }

  @Override
  public synchronized void start() {
    super.start();
    // TODO
  }

  @Override
  public synchronized void stop() {
    // TODO
    super.stop();
  }

  public ExecuteStatementOperation newExecuteStatementOperation(HiveSession parentSession,
      String statement, Map<String, String> confOverlay, boolean runAsync) {
    ExecuteStatementOperation executeStatementOperation = ExecuteStatementOperation
        .newExecuteStatementOperation(parentSession, statement, confOverlay, runAsync);
    addOperation(executeStatementOperation);
    return executeStatementOperation;
  }

  public GetTypeInfoOperation newGetTypeInfoOperation(HiveSession parentSession) {
    GetTypeInfoOperation operation = new GetTypeInfoOperation(parentSession);
    addOperation(operation);
    return operation;
  }

  public GetCatalogsOperation newGetCatalogsOperation(HiveSession parentSession) {
    GetCatalogsOperation operation = new GetCatalogsOperation(parentSession);
    addOperation(operation);
    return operation;
  }

  public GetSchemasOperation newGetSchemasOperation(HiveSession parentSession,
      String catalogName, String schemaName) {
    GetSchemasOperation operation = new GetSchemasOperation(parentSession, catalogName, schemaName);
    addOperation(operation);
    return operation;
  }

  public MetadataOperation newGetTablesOperation(HiveSession parentSession,
      String catalogName, String schemaName, String tableName,
      List<String> tableTypes) {
    MetadataOperation operation =
        new GetTablesOperation(parentSession, catalogName, schemaName, tableName, tableTypes);
    addOperation(operation);
    return operation;
  }

  public GetTableTypesOperation newGetTableTypesOperation(HiveSession parentSession) {
    GetTableTypesOperation operation = new GetTableTypesOperation(parentSession);
    addOperation(operation);
    return operation;
  }

  public GetColumnsOperation newGetColumnsOperation(HiveSession parentSession,
      String catalogName, String schemaName, String tableName, String columnName) {
    GetColumnsOperation operation = new GetColumnsOperation(parentSession,
        catalogName, schemaName, tableName, columnName);
    addOperation(operation);
    return operation;
  }

  public GetFunctionsOperation newGetFunctionsOperation(HiveSession parentSession,
      String catalogName, String schemaName, String functionName) {
    GetFunctionsOperation operation = new GetFunctionsOperation(parentSession,
        catalogName, schemaName, functionName);
    addOperation(operation);
    return operation;
  }

    //一个session对象以及对应的操作类型,返回该操作对象
  public synchronized Operation getOperation(OperationHandle operationHandle) throws HiveSQLException {
    Operation operation = handleToOperation.get(operationHandle);
    if (operation == null) {
      throw new HiveSQLException("Invalid OperationHandle: " + operationHandle);
    }
    return operation;
  }

  private synchronized void addOperation(Operation operation) {
    handleToOperation.put(operation.getHandle(), operation);
  }

  private synchronized Operation removeOperation(OperationHandle opHandle) {
    return handleToOperation.remove(opHandle);
  }

  //返回操作的状态
  public OperationState getOperationState(OperationHandle opHandle) throws HiveSQLException {
    return getOperation(opHandle).getState();
  }

  //取消该操作
  public void cancelOperation(OperationHandle opHandle) throws HiveSQLException {
    getOperation(opHandle).cancel();
  }

  //关闭该操作
  public void closeOperation(OperationHandle opHandle) throws HiveSQLException {
    Operation operation = removeOperation(opHandle);
    if (operation == null) {
      throw new HiveSQLException("Operation does not exist!");
    }
    operation.close();
  }

  //返回操作的结果schema
  public TableSchema getOperationResultSetSchema(OperationHandle opHandle)
      throws HiveSQLException {
    return getOperation(opHandle).getResultSetSchema();
  }

  //返回操作的结果集
  public RowSet getOperationNextRowSet(OperationHandle opHandle) throws HiveSQLException {
    return getOperation(opHandle).getNextRowSet();
  }

  public RowSet getOperationNextRowSet(OperationHandle opHandle,
      FetchOrientation orientation, long maxRows)
      throws HiveSQLException {
    return getOperation(opHandle).getNextRowSet(orientation, maxRows);
  }
}
