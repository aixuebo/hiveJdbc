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

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * UDFLpad.
 * 左边填补
 * lpad('hi', 5, '?!') 表示在左边填补?!,一共最终达到5位即可,返回?!?hi
 * lpad('hi', 1, '??') 因为hi大于1,因此不需要填补,返回h
 * 
 * 注意:如果三个参数有任意一个是null,则都不允许,结果返回null
 */
@Description(name = "lpad",
    value = "_FUNC_(str, len, pad) - Returns str, left-padded with pad to a length of len",
    extended = "If str is longer than len, the return value is shortened to "
    + "len characters.\n"
    + "Example:\n"
    + "  > SELECT _FUNC_('hi', 5, '??') FROM src LIMIT 1;\n"
    + "  '???hi'"
    + "  > SELECT _FUNC_('hi', 1, '??') FROM src LIMIT 1;\n" + "  'h'")
public class UDFLpad extends UDF {

  private final Text result = new Text();

  //如果三个参数有任意一个是null,则都不允许,结果返回null
  public Text evaluate(Text s, IntWritable n, Text pad) {
    if (s == null || n == null || pad == null) {
      return null;
    }

    //获取最终需要多少个字节
    int len = n.get();

    byte[] data = result.getBytes();
    if (data.length < len) {//如果需要的长度len比现在缓冲的大,则说明本次需要截取的缓冲字节不够,要扩容
      data = new byte[len];
    }

    byte[] txt = s.getBytes();//原始字符串
    byte[] padTxt = pad.getBytes();//填充内容

    // The length of the padding needed 计算需要填充多少个字节,不需要填充则返回0
    int pos = Math.max(len - s.getLength(), 0);

    // Copy the padding
    for (int i = 0; i < pos; i += pad.getLength()) {//循环padText内容,直到填充满pos个元素为止
      for (int j = 0; j < pad.getLength() && j < pos - i; j++) {//依次填充内容
        data[i + j] = padTxt[j];
      }
    }

    // Copy the text填充具体的txt内容
    for (int i = 0; pos + i < len && i < s.getLength(); i++) {
      data[pos + i] = txt[i];
    }

    //将填充的结果返回
    result.set(data, 0, len);
    return result;
  }
}
