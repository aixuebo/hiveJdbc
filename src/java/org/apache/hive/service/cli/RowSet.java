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

import org.apache.hive.service.cli.thrift.TRow;
import org.apache.hive.service.cli.thrift.TRowSet;

/**
 * RowSet.
 * 存储一行一行的数据
 */
public class RowSet {

  private long startOffset = 0;//表示此时结果集的开始第一条数据是全部历史数据中的第几条
  private boolean hasMoreResults = false;
  private List<Row> rows;//存储尚未发送给客户端的所有数据行

  public RowSet() {
    rows = new ArrayList<Row>();
  }

  public RowSet(TRowSet tRowSet) {
    this();
    startOffset = tRowSet.getStartRowOffset();
    for (TRow tRow : tRowSet.getRows()) {
      rows.add(new Row(tRow));
    }
  }

  public RowSet(List<Row> rows, long startOffset) {
    this();
    this.rows.addAll(rows);
    this.startOffset = startOffset;
  }

    //结果集添加一行数据
  public RowSet addRow(Row row) {
    rows.add(row);
    return this;
  }

  //提供一行的所有数据,自动添加一行数据
  public RowSet addRow(TableSchema schema, Object[] fields) {
    return addRow(new Row(schema, fields));
  }

  //从数据结果集中抽取maxRows行数据返回给客户端
  public RowSet extractSubset(int maxRows) {
    int numRows = rows.size();//总行数
    maxRows = (maxRows <= numRows) ? maxRows : numRows;//真正要抓取多少行数据
    RowSet result = new RowSet(rows.subList(0, maxRows), startOffset);//从结果集中抓取maxRows数据
    rows = new ArrayList<Row>(rows.subList(maxRows, numRows));//因为rows数据已经抓取了maxRows条了,因此要删除这些条,即从maxRows之后的数据是尚未被抓取的.要保留
    startOffset += result.getSize();//最近的位置
    return result;
  }

  public long getStartOffset() {
    return startOffset;
  }

  public RowSet setStartOffset(long startOffset) {
    this.startOffset = startOffset;
    return this;
  }

  public boolean getHasMoreResults() {
    return hasMoreResults;
  }

  public RowSet setHasMoreResults(boolean hasMoreResults) {
    this.hasMoreResults = hasMoreResults;
    return this;
  }

  public int getSize() {
    return rows.size();
  }

  public TRowSet toTRowSet() {
    TRowSet tRowSet = new TRowSet();
    tRowSet.setStartRowOffset(startOffset);
    List<TRow> tRows = new ArrayList<TRow>();
    for (Row row : rows) {
      tRows.add(row.toTRow());
    }
    tRowSet.setRows(tRows);

    /*
    //List<Boolean> booleanColumn = new ArrayList<Boolean>();
    //List<Byte> byteColumn = new ArrayList<Byte>();
    //List<Short> shortColumn = new ArrayList<Short>();
    List<Integer> integerColumn = new ArrayList<Integer>();

    integerColumn.add(1);
    //integerColumn.add(null);
    integerColumn.add(3);
    //integerColumn.add(null);


    TColumnUnion column = TColumnUnion.i32Column(integerColumn);
    List<TColumnUnion> columns = new ArrayList<TColumnUnion>();
    columns.add(column);
    tRowSet.setColumns(columns);
    */

    return tRowSet;
  }
}
