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
 * 属性规则攻击
 */
public final class ColumnProjectionUtils {

  public static final String READ_COLUMN_IDS_CONF_STR = "hive.io.file.readcolumn.ids";//将整数集合转化为逗号拆分的字符串,比如2,3,4,5,并且存储该字符串
  public static final String READ_COLUMN_NAMES_CONF_STR = "hive.io.file.readcolumn.names";

  /**
   * Sets read columns' ids(start from zero) for RCFile's Reader. Once a column
   * is included in the list, RCFile's reader will not skip its value.
   * 重新设置ids集合,覆盖操作
   */
  public static void setReadColumnIDs(Configuration conf, List<Integer> ids) {
    String id = toReadColumnIDString(ids);//将整数集合转化为逗号拆分的字符串,比如2,3,4,5
    setReadColumnIDConf(conf, id);//设置ids到配置文件中
  }

  /**
   * Sets read columns' ids(start from zero) for RCFile's Reader. Once a column
   * is included in the list, RCFile's reader will not skip its value.
   * 重新设置ids集合,追加ids集合
   */
  public static void appendReadColumnIDs(Configuration conf, List<Integer> ids) {
    String id = toReadColumnIDString(ids);//将整数集合转化为逗号拆分的字符串,比如2,3,4,5
    if (id != null) {
      String old = conf.get(READ_COLUMN_IDS_CONF_STR, null);///获取以前设置的
      String newConfStr = id;
      if (old != null) {//新的id集合和老的id集合汇总
        newConfStr = newConfStr + StringUtils.COMMA_STR + old;
      }

      //追加两个汇总ids集合到conf中
      setReadColumnIDConf(conf, newConfStr);
    }
  }

  /**
   * 重新设置ids集合,追加ids集合,返回aa,bb,cc
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
          result.append(',');//追加,号
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
   * 将整数集合转化为逗号拆分的字符串,比如2,3,4,5
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
   * 将ids的字符串格式转化成集合
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
   * 设置为空
   */
  public static void setFullyReadColumns(Configuration conf) {
    conf.set(READ_COLUMN_IDS_CONF_STR, "");
  }

  private ColumnProjectionUtils() {
    // prevent instantiation
  }

}
