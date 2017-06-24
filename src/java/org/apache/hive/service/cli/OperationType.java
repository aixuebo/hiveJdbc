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
 * ��������
 */
public enum OperationType {

  UNKNOWN_OPERATION(TOperationType.UNKNOWN),//δ֪
  EXECUTE_STATEMENT(TOperationType.EXECUTE_STATEMENT),//����һЩ�������
  GET_TYPE_INFO(TOperationType.GET_TYPE_INFO),//��ȡhive֧�ֵ��ֶ������Լ����׼sql�ӿڵ����ͶԱ�
  GET_CATALOGS(TOperationType.GET_CATALOGS),//��ȡCatalog name�ı��ʽ,Ŀǰ��ʵ�ָ�Ԫ����,��Ϊ��Ԫ���ݱ����ݿ�ļ��𻹴�,���û��Ҫ
  GET_SCHEMAS(TOperationType.GET_SCHEMAS),//���ݸ��������ݿ���ʽ,��ȡ���ݿ��Ԫ����
  GET_TABLES(TOperationType.GET_TABLES),//��ȡtable���Ԫ����
  GET_TABLE_TYPES(TOperationType.GET_TABLE_TYPES),//��ȡ�������MANAGED_TABLE��EXTERNAL_TABLE��VIRTUAL_VIEW��INDEX_TABLE
  GET_COLUMNS(TOperationType.GET_COLUMNS),//���ݸ����������ʽ---���ݿ⡢table����,��ȡ������ʽ���еļ���,���ö����ʾ��λ�ȡ�е�Ԫ����
  GET_FUNCTIONS(TOperationType.GET_FUNCTIONS);//��ȡƥ��ĺ������ʽ��Ӧ�ĺ�������

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
