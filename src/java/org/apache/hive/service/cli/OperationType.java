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

import org.apache.hive.service.cli.thrift.TOperationType;

/**
 * OperationType.
 * 操作类型
 */
public enum OperationType {

  UNKNOWN_OPERATION(TOperationType.UNKNOWN),//未知
  EXECUTE_STATEMENT(TOperationType.EXECUTE_STATEMENT),//运行一些命令操作
  GET_TYPE_INFO(TOperationType.GET_TYPE_INFO),//获取hive支持的字段类型以及与标准sql接口的类型对比
  GET_CATALOGS(TOperationType.GET_CATALOGS),//获取Catalog name的表达式,目前不实现该元数据,因为该元数据比数据库的级别还大,因此没必要
  GET_SCHEMAS(TOperationType.GET_SCHEMAS),//根据给定的数据库表达式,获取数据库的元数据
  GET_TABLES(TOperationType.GET_TABLES),//获取table表的元数据
  GET_TABLE_TYPES(TOperationType.GET_TABLE_TYPES),//获取表的类型MANAGED_TABLE、EXTERNAL_TABLE、VIRTUAL_VIEW、INDEX_TABLE
  GET_COLUMNS(TOperationType.GET_COLUMNS),//根据给定三个表达式---数据库、table表、列,获取满足表达式的列的集合,即该对象表示如何获取列的元数据
  GET_FUNCTIONS(TOperationType.GET_FUNCTIONS);//获取匹配的函数表达式对应的函数集合

  private TOperationType tOperationType;

  OperationType(TOperationType tOpType) {
    this.tOperationType = tOpType;
  }

  public static OperationType getOperationType(TOperationType tOperationType) {
    // TODO: replace this with a Map?
    for (OperationType opType : values()) {
      if (tOperationType.equals(opType.tOperationType)) {
        return opType;
      }
    }
    return OperationType.UNKNOWN_OPERATION;
  }

  public TOperationType toTOperationType() {
    return tOperationType;
  }
}
