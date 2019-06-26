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

package org.apache.hadoop.hive.ql.udf;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * UDF to extract a specific group identified by a java regex. Note that if a
 * regexp has a backslash ('\'), then need to specify '\\' For example,
 * 使用正则表达式抽取指定字符串中匹配的关键信息,返回某一个匹配的信息
 * regexp_extract(string,regexp,index) 返回匹配正则表达式的第index个group,默认index可以省略,默认为1
 * 例如
 * regexp_extract('100-200', '(\\d+)-(\\d+)', 1) will return '100'  例子从100-200中获取第一个分组100

message = "[字段变更] xxxx: => 5.5;[字段变更] ";
String str = evaluate(message,"xxxx\\: \\=> (.*)\\;",1);  返回5.5
正则表达式 不知道为什么要对; : =进行转义，如果纯java是不需要的，但是hive传进来就报错，需要转义。
 */
@Description(name = "regexp_extract",
    value = "_FUNC_(str, regexp[, idx]) - extracts a group that matches regexp",
    extended = "Example:\n"
    + "  > SELECT _FUNC_('100-200', '(\\d+)-(\\d+)', 1) FROM src LIMIT 1;\n"
    + "  '100'")
public class UDFRegExpExtract extends UDF {
  private String lastRegex = null;
  private Pattern p = null;

  public UDFRegExpExtract() {
  }

  /**
   * 
   * @param s 字符串
   * @param regex 带()组成的分组好的正则表达式
   * @param extractIndex 最终获取的是第几个分组值,最小值以1开始计算
   * @return
   */
  public String evaluate(String s, String regex, Integer extractIndex) {
    if (s == null || regex == null) {
      return null;
    }
    if (!regex.equals(lastRegex) || p == null) {
      lastRegex = regex;
      p = Pattern.compile(regex);
    }
    Matcher m = p.matcher(s);
    if (m.find()) {
      MatchResult mr = m.toMatchResult();
      return mr.group(extractIndex);
    }
    return "";
  }

  public String evaluate(String s, String regex) {
    return this.evaluate(s, regex, 1);
  }

}
