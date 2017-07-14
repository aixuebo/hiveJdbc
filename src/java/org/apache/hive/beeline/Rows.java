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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Abstract base class representing a set of rows to be displayed.
 * Holds column values as strings
 * ��ʾһ���еļ���
 */
abstract class Rows implements Iterator {
  private final BeeLine beeLine;
  final ResultSetMetaData rsMeta;
  final Boolean[] primaryKeys;//ÿһ�����Ƿ�������,true��ʾ������
  final NumberFormat numberFormat;//Number���͵������ת��
  private final String nullStr;

  Rows(BeeLine beeLine, ResultSet rs) throws SQLException {
    this.beeLine = beeLine;
    nullStr = beeLine.getOpts().getNullString();
    rsMeta = rs.getMetaData();
    int count = rsMeta.getColumnCount();//�е�����
    primaryKeys = new Boolean[count];
    if (beeLine.getOpts().getNumberFormat().equals("default")) {
      numberFormat = null;
    } else {
      numberFormat = new DecimalFormat(beeLine.getOpts().getNumberFormat());
    }
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Update all of the rows to have the same size, set to the
   * maximum length of each column in the Rows.
   */
  abstract void normalizeWidths();

  /**
   * Return whether the specified column (0-based index) is
   * a primary key. Since this method depends on whether the
   * JDBC driver property implements {@link ResultSetMetaData#getTableName} (many do not), it
   * is not reliable for all databases.
   * ��ȡ�����Ƿ�������,�������ø����Ƿ�������
   */
  boolean isPrimaryKey(int col) {
    if (primaryKeys[col] != null) {
      return primaryKeys[col].booleanValue();
    }

    try {
      // this doesn't always work, since some JDBC drivers (e.g.,
      // Oracle's) return a blank string from getTableName.
      String table = rsMeta.getTableName(col + 1);
      String column = rsMeta.getColumnName(col + 1);

      if (table == null || table.length() == 0 ||
          column == null || column.length() == 0) {
        return (primaryKeys[col] = new Boolean(false)).booleanValue();//������д���,��϶���������
      }

      ResultSet pks = beeLine.getDatabaseConnection().getDatabaseMetaData().getPrimaryKeys(
          beeLine.getDatabaseConnection().getDatabaseMetaData().getConnection().getCatalog(), null, table);//��ȡ�ñ�����������

      try {
        while (pks.next()) {
          if (column.equalsIgnoreCase(
              pks.getString("COLUMN_NAME"))) {//��ȡ���ؽ�����е�����name,�ж��Ƿ��Ǵ�ʱ����name
            return (primaryKeys[col] = new Boolean(true)).booleanValue();//˵��������
          }
        }
      } finally {
        pks.close();
      }

      return (primaryKeys[col] = new Boolean(false)).booleanValue();
    } catch (SQLException sqle) {
      return (primaryKeys[col] = new Boolean(false)).booleanValue();
    }
  }


  class Row {//��ʾһ������
    final String[] values;//�洢����
    final boolean isMeta;
    boolean deleted;
    boolean inserted;
    boolean updated;
    int[] sizes;//ÿһ���ֶε�ֵ�ĳ���

      //�洢title��Ϣ
    Row(int size) throws SQLException {
      isMeta = true;
      values = new String[size];//title����
      sizes = new int[size];//ÿһ��titleռ�ö��ٸ�����
      for (int i = 0; i < size; i++) {
        values[i] = rsMeta.getColumnLabel(i + 1);
        sizes[i] = values[i] == null ? 1 : values[i].length();
      }

      deleted = false;
      updated = false;
      inserted = false;
    }

    @Override
    public String toString(){
      return Arrays.asList(values).toString();
    }

    Row(int size, ResultSet rs) throws SQLException {
      isMeta = false;
      values = new String[size];
      sizes = new int[size];

      try {
        deleted = rs.rowDeleted();
      } catch (Throwable t) {
      }
      try {
        updated = rs.rowUpdated();
      } catch (Throwable t) {
      }
      try {
        inserted = rs.rowInserted();
      } catch (Throwable t) {
      }

      for (int i = 0; i < size; i++) {
        if (numberFormat != null) {
          Object o = rs.getObject(i + 1);
          if (o == null) {
            values[i] = null;
          }  else if (o instanceof Number) {
            values[i] = numberFormat.format(o);
          } else {
            values[i] = o.toString();
          }
        } else {
          values[i] = rs.getString(i + 1);
        }
        values[i] = values[i] == null ? nullStr : values[i];
        sizes[i] = values[i].length();
      }
    }
  }
}
