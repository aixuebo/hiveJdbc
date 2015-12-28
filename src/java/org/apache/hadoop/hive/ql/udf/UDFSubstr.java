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

import java.util.Arrays;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * UDFSubstr.
 * 字符串截取Facebook,
 * 如果要从5开始截取.则是从1开始计算字符串的.[5,因此第五个是要被保留的
 * 如果从-5开始截取,从后面查,后面自后一个也是1,例如-5,从后查询[5,因此第五个是要被保留的
 * 
 * substr(String,int pos,int lenth) 截取字符串
  例如:substr("abcd",1,2) 返回ab
    substr("abcd",2) 返回bcd,表示从2开始一直到结尾
 注意,pos下标从1开始计算,length表示最终获取多少个数字
 */
@Description(name = "substr,substring",
    value = "_FUNC_(str, pos[, len]) - returns the substring of str that"
    + " starts at pos and is of length len or" +
    "_FUNC_(bin, pos[, len]) - returns the slice of byte array that"
    + " starts at pos and is of length len",
    extended = "pos is a 1-based index. If pos<0 the starting position is"
    + " determined by counting backwards from the end of str.\n"
    + "Example:\n "
    + "  > SELECT _FUNC_('Facebook', 5) FROM src LIMIT 1;\n"
    + "  'book'\n"
    + "  > SELECT _FUNC_('Facebook', -5) FROM src LIMIT 1;\n"
    + "  'ebook'\n"
    + "  > SELECT _FUNC_('Facebook', 5, 1) FROM src LIMIT 1;\n"
    + "  'b'")
public class UDFSubstr extends UDF {

  private final int[] index;
  private final Text r;

  public UDFSubstr() {
    index = new int[2];
    r = new Text();
  }

  /**
   * 
   * @param t 等待截取的字符串
   * @param pos 从开始位置截取
   * @param len 截取多少个字符
   * @return
   */
  public Text evaluate(Text t, IntWritable pos, IntWritable len) {

    if ((t == null) || (pos == null) || (len == null)) {
      return null;
    }

    r.clear();
    if ((len.get() <= 0)) {
      return r;
    }

    String s = t.toString();
    int[] index = makeIndex(pos.get(), len.get(), s.length());
    if (index == null) {
      return r;
    }

    r.set(s.substring(index[0], index[1]));
    return r;
  }

  /**
   * 
   * @param pos 从开始位置截取
   * @param len 截取的总长度
   * @param inputLen 等待截取的字符串总长度
   * @return
   */
  private int[] makeIndex(int pos, int len, int inputLen) {
    if ((Math.abs(pos) > inputLen)) {//说明不需要截取,已经超出总长度了
      return null;
    }

    int start, end;

    if (pos > 0) {
      start = pos - 1;
    } else if (pos < 0) {
      start = inputLen + pos;
    } else {
      start = 0;
    }

    if ((inputLen - start) < len) {
      end = inputLen;//最后一个位置,因为字符串的总长度不够
    } else {
      end = start + len;
    }
    index[0] = start;
    index[1] = end;
    return index;
  }

  private final IntWritable maxValue = new IntWritable(Integer.MAX_VALUE);

  public Text evaluate(Text s, IntWritable pos) {
    return evaluate(s, pos, maxValue);
  }

  public BytesWritable evaluate(BytesWritable bw, IntWritable pos, IntWritable len) {

    if ((bw == null) || (pos == null) || (len == null)) {
      return null;
    }

    if ((len.get() <= 0)) {
      return new BytesWritable();
    }

    int[] index = makeIndex(pos.get(), len.get(), bw.getLength());
    if (index == null) {
      return new BytesWritable();
    }

    return new BytesWritable(Arrays.copyOfRange(bw.getBytes(), index[0], index[1]));
  }

  public BytesWritable evaluate(BytesWritable bw, IntWritable pos){
    return evaluate(bw, pos, maxValue);
  }
}
