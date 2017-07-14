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

/*
 * This source file is based on code taken from SQLLine 1.0.2
 * See SQLLine notice in LICENSE
 */
package org.apache.hive.beeline;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Rows implementation which buffers all rows in a linked list.
 * 将所有的数据存储到list中缓存起来,可以不断的进行循环,而不需要每次都请求数据库
 * 缺点是耗费内存,如果数据量大,会导致OOM
 */
class BufferedRows extends Rows {
  private final LinkedList<Row> list;//title和数据内容,每一个元素是一行数据
  private final Iterator<Row> iterator;//数据list的迭代器

  BufferedRows(BeeLine beeLine, ResultSet rs) throws SQLException {
    super(beeLine, rs);
    list = new LinkedList<Row>();
    int count = rsMeta.getColumnCount();
    list.add(new Row(count));//设置title
    while (rs.next()) {
      list.add(new Row(count, rs));//设置每一行数据
    }
    iterator = list.iterator();
  }

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public Object next() {
    return iterator.next();
  }

  @Override
  public String toString(){
    return list.toString();
  }

  @Override
  void normalizeWidths() {
    int[] max = null;
    for (Row row : list) {
      if (max == null) {
        max = new int[row.values.length];
      }
      for (int j = 0; j < max.length; j++) {
        max[j] = Math.max(max[j], row.sizes[j] + 1);
      }
    }
    for (Row row : list) {
      row.sizes = max;
    }
  }

}
