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

import java.util.List;

import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;

//自动补全表名字
class TableNameCompletor implements Completer {
  private final BeeLine beeLine;

  /**
   * @param beeLine
   */
  TableNameCompletor(BeeLine beeLine) {
    this.beeLine = beeLine;
  }

    /**
     *
     * @param buf buffer是当前用户输入的内容
     * @param pos cursor表示光标的位置
     * @param cand 最终推荐的匹配的候选集合
     * @return 返回从cand候选集合中查找合适的第几个元素作为返回值
     */
  public int complete(String buf, int pos, List cand) {
    if (beeLine.getDatabaseConnection() == null) {//必须要有数据库连接信息
      return -1;
    }
    return new StringsCompleter(beeLine.getDatabaseConnection().getTableNames(true))//获取全部的表集合
        .complete(buf, pos, cand);//在表集合中进行补全buffer的匹配的内容
  }
}