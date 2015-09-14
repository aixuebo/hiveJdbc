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

package org.apache.hadoop.hive.serde2;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.StringUtils;

/**
 * ColumnProjectionUtils.
 * ���Թ��򹥻�
 */
public final class ColumnProjectionUtils {

  public static final String READ_COLUMN_IDS_CONF_STR = "hive.io.file.readcolumn.ids";//����������ת��Ϊ���Ų�ֵ��ַ���,����2,3,4,5,���Ҵ洢���ַ���
  public static final String READ_COLUMN_NAMES_CONF_STR = "hive.io.file.readcolumn.names";

  /**
   * Sets read columns' ids(start from zero) for RCFile's Reader. Once a column
   * is included in the list, RCFile's reader will not skip its value.
   * ��������ids����,���ǲ���
   */
  public static void setReadColumnIDs(Configuration conf, List<Integer> ids) {
    String id = toReadColumnIDString(ids);//����������ת��Ϊ���Ų�ֵ��ַ���,����2,3,4,5
    setReadColumnIDConf(conf, id);//����ids�������ļ���
  }

  /**
   * Sets read columns' ids(start from zero) for RCFile's Reader. Once a column
   * is included in the list, RCFile's reader will not skip its value.
   * ��������ids����,׷��ids����
   */
  public static void appendReadColumnIDs(Configuration conf, List<Integer> ids) {
    String id = toReadColumnIDString(ids);//����������ת��Ϊ���Ų�ֵ��ַ���,����2,3,4,5
    if (id != null) {
      String old = conf.get(READ_COLUMN_IDS_CONF_STR, null);///��ȡ��ǰ���õ�
      String newConfStr = id;
      if (old != null) {//�µ�id���Ϻ��ϵ�id���ϻ���
        newConfStr = newConfStr + StringUtils.COMMA_STR + old;
      }

      //׷����������ids���ϵ�conf��
      setReadColumnIDConf(conf, newConfStr);
    }
  }

  /**
   * ��������ids����,׷��ids����,����aa,bb,cc
   * @param conf
   * @param cols
   */
  public static void appendReadColumnNames(Configuration conf,
                                           List<String> cols) {
    if (cols != null) {
      String old = conf.get(READ_COLUMN_NAMES_CONF_STR, "");
      StringBuilder result = new StringBuilder(old);
      boolean first = old.isEmpty();
      for(String col: cols) {
        if (first) {
          first = false;
        } else {
          result.append(',');//׷��,��
        }
        result.append(col);
      }
      conf.set(READ_COLUMN_NAMES_CONF_STR, result.toString());
    }
  }

  private static void setReadColumnIDConf(Configuration conf, String id) {
    if (id == null || id.length() <= 0) {
      conf.set(READ_COLUMN_IDS_CONF_STR, "");
      return;
    }

    conf.set(READ_COLUMN_IDS_CONF_STR, id);
  }

  /**
   * ����������ת��Ϊ���Ų�ֵ��ַ���,����2,3,4,5
   */
  private static String toReadColumnIDString(List<Integer> ids) {
    String id = null;
    if (ids != null) {
      for (int i = 0; i < ids.size(); i++) {
        if (i == 0) {
          id = "" + ids.get(i);
        } else {
          id = id + StringUtils.COMMA_STR + ids.get(i);
        }
      }
    }
    return id;
  }

  /**
   * Returns an array of column ids(start from zero) which is set in the given
   * parameter <tt>conf</tt>.
   * ��ids���ַ�����ʽת���ɼ���
   */
  public static ArrayList<Integer> getReadColumnIDs(Configuration conf) {
    if (conf == null) {
      return new ArrayList<Integer>(0);
    }
    String skips = conf.get(READ_COLUMN_IDS_CONF_STR, "");
    String[] list = StringUtils.split(skips);
    ArrayList<Integer> result = new ArrayList<Integer>(list.length);
    for (String element : list) {
      // it may contain duplicates, remove duplicates
      Integer toAdd = Integer.parseInt(element);
      if (!result.contains(toAdd)) {
        result.add(toAdd);
      }
    }
    return result;
  }

  /**
   * Clears the read column ids set in the conf, and will read all columns.
   * ����Ϊ��
   */
  public static void setFullyReadColumns(Configuration conf) {
    conf.set(READ_COLUMN_IDS_CONF_STR, "");
  }

  private ColumnProjectionUtils() {
    // prevent instantiation
  }

}
