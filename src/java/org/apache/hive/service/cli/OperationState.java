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

import org.apache.hive.service.cli.thrift.TOperationState;

/**
 * OperationState.
 * 操作的状态
 */
public enum OperationState {
  INITIALIZED(TOperationState.INITIALIZED_STATE),//初始化完成
  RUNNING(TOperationState.RUNNING_STATE),//运行中
  FINISHED(TOperationState.FINISHED_STATE),//完成
  CANCELED(TOperationState.CANCELED_STATE),//取消
  CLOSED(TOperationState.CLOSED_STATE),//关闭
  ERROR(TOperationState.ERROR_STATE),//异常
  UNKNOWN(TOperationState.UKNOWN_STATE),//未知
  PENDING(TOperationState.PENDING_STATE);//等待

  private final TOperationState tOperationState;//thrift的状态对象

  OperationState(TOperationState tOperationState) {
    this.tOperationState = tOperationState;
  }

 //thrift对象和hive对象转换
  public static OperationState getOperationState(TOperationState tOperationState) {
    // TODO: replace this with a Map?
    for (OperationState opState : values()) {
      if (tOperationState.equals(opState.tOperationState)) {
        return opState;
      }
    }
    return OperationState.UNKNOWN;
  }

  //状态机
  public static void validateTransition(OperationState oldState, OperationState newState)
      throws HiveSQLException {
    switch (oldState) {
    case INITIALIZED:
      switch (newState) {
      case PENDING:
      case RUNNING:
      case CLOSED:
        return;
      }
      break;
    case PENDING:
      switch (newState) {
      case RUNNING:
      case FINISHED:
      case CANCELED:
      case ERROR:
      case CLOSED:
        return;
      }
      break;
    case RUNNING:
      switch (newState) {
      case FINISHED:
      case CANCELED:
      case ERROR:
      case CLOSED:
        return;
      }
      break;
    case FINISHED:
    case CANCELED:
    case ERROR:
      if (OperationState.CLOSED.equals(newState)) {
        return;
      }
    default:
      // fall-through
    }
    throw new HiveSQLException("Illegal Operation state transition");//状态异常
  }

  public void validateTransition(OperationState newState)
  throws HiveSQLException {
    validateTransition(this, newState);
  }

  public TOperationState toTOperationState() {
    return tOperationState;
  }
}
