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

import java.util.ArrayList;
import java.util.List;

import org.apache.hive.service.cli.thrift.TColumnValue;
import org.apache.hive.service.cli.thrift.TRow;

/**
 * Row.
 * 代表一行数据
 */
public class Row {
  private final List<ColumnValue> values = new ArrayList<ColumnValue>();//存储一行内的数据

  public Row() {
  }

  public Row(TRow tRow) {
    for (TColumnValue tColumnValues : tRow.getColVals()) {
      values.add(new ColumnValue(tColumnValues));
    }
  }

    /**
     * 代表一行数据
     * @param schema 该数据代码的schema
     * @param fields 该行数据中每一个列的值
     */
  public Row(TableSchema schema, Object[] fields) {
    assert fields.length == schema.getColumnDescriptors().size();//确保列的数量与参数field数量一样
    for (ColumnDescriptor colDesc : schema.getColumnDescriptors()) {//循环每一列
      TypeDescriptor typeDesc = colDesc.getTypeDescriptor();
      values.add(ColumnValue.newColumnValue(typeDesc.getType(), fields[colDesc.getOrdinalPosition() - 1]));//使用列的类型以及列对应的值,创建一个ColumnValue
    }
  }

  //添加一列
  public Row addColumnValue(ColumnValue value) {
    values.add(value);
    return this;
  }

  //添加一列,该列类型是boolean
  public Row addBoolean(boolean value) {
    values.add(ColumnValue.booleanValue(value));
    return this;
  }

  public Row addByte(byte value) {
    values.add(ColumnValue.byteValue(value));
    return this;
  }

  public Row addString(String value) {
    values.add(ColumnValue.stringValue(value));
    return this;
  }

  public TRow toTRow() {
    TRow tRow = new TRow();
    for (ColumnValue columnValue : values) {
      if (columnValue != null) {
        tRow.addToColVals(columnValue.toTColumnValue());
      } else {
        tRow.addToColVals(ColumnValue.NULL);
      }
    }
    return tRow;
  }
}
