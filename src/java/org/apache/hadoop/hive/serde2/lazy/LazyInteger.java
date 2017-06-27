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
package org.apache.hadoop.hive.serde2.lazy;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.hive.serde2.lazy.objectinspector.primitive.LazyIntObjectInspector;
import org.apache.hadoop.io.IntWritable;

/**
 * LazyObject for storing a value of Integer.
 * 
 * <p>
 * Part of the code is adapted from Apache Harmony Project.
 * 
 * As with the specification, this implementation relied on code laid out in <a
 * href="http://www.hackersdelight.org/">Henry S. Warren, Jr.'s Hacker's
 * Delight, (Addison Wesley, 2002)</a> as well as <a
 * href="http://aggregate.org/MAGIC/">The Aggregate's Magic Algorithms</a>.
 * </p>
 *
 * 懒加载int类型的值,LazyIntObjectInspector表示int类型,IntWritable表示int类型的值对应的类型
 */
public class LazyInteger extends
    LazyPrimitive<LazyIntObjectInspector, IntWritable> {

  public LazyInteger(LazyIntObjectInspector oi) {
    super(oi);
    data = new IntWritable();
  }

  public LazyInteger(LazyInteger copy) {
    super(copy);
    data = new IntWritable(copy.data.get());
  }

  @Override
  public void init(ByteArrayRef bytes, int start, int length) {
    try {
      data.set(parseInt(bytes.getData(), start, length, 10));
      isNull = false;
    } catch (NumberFormatException e) {
      isNull = true;
      logExceptionMessage(bytes, start, length, "INT");
    }
  }

  /**
   * Parses the string argument as if it was an int value and returns the
   * result. Throws NumberFormatException if the string does not represent an
   * int quantity.
   * 
   * @param bytes
   * @param start
   * @param length
   *          a UTF-8 encoded string representation of an int quantity.
   * @return int the value represented by the argument
   * @exception NumberFormatException
   *              if the argument could not be parsed as an int quantity.
   */
  public static int parseInt(byte[] bytes, int start, int length) {
    return parseInt(bytes, start, length, 10);
  }

  /**
   * Parses the string argument as if it was an int value and returns the
   * result. Throws NumberFormatException if the string does not represent an
   * int quantity. The second argument specifies the radix to use when parsing
   * the value.
   * 
   * @param bytes
   * @param start
   * @param length
   *          a UTF-8 encoded string representation of an int quantity.
   * @param radix
   *          the base to use for conversion.
   * @return the value represented by the argument
   * @exception NumberFormatException
   *              if the argument could not be parsed as an int quantity.
   */
  public static int parseInt(byte[] bytes, int start, int length, int radix) {
    if (bytes == null) {
      throw new NumberFormatException("String is null");
    }
    if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
      throw new NumberFormatException("Invalid radix: " + radix);
    }
    if (length == 0) {
      throw new NumberFormatException("Empty string!");
    }
    int offset = start;
    boolean negative = bytes[start] == '-';//说明是负数
    if (negative || bytes[start] == '+') {//取消正负号
      offset++;
      if (length == 1) {
        throw new NumberFormatException(LazyUtils.convertToString(bytes, start,
            length));
      }
    }

    return parse(bytes, start, length, offset, radix, negative);
  }

  /**
   * 
   * @param bytes
   * @param start
   * @param length
   *          a UTF-8 encoded string representation of an int quantity.
   * @param radix
   *          the base to use for conversion.
   * @param offset
   *          the starting position after the sign (if exists)
   * @param radix
   *          the base to use for conversion.
   * @param negative
   *          whether the number is negative.
   * @return the value represented by the argument
   * @exception NumberFormatException
   *              if the argument could not be parsed as an int quantity.
   * 如何反序列化
   */
  private static int parse(byte[] bytes, int start, int length, int offset,
      int radix, boolean negative) {
    byte separator = '.';
    int max = Integer.MIN_VALUE / radix;//最大值
    int result = 0, end = start + length;
    while (offset < end) {//获取整数部分
      int digit = LazyUtils.digit(bytes[offset++], radix);//返回该位置byte对应的数字
      if (digit == -1) {
        if (bytes[offset-1] == separator) {//说明遇见.了
          // We allow decimals and will return a truncated integer in that case.
          // Therefore we won't throw an exception here (checking the fractional
          // part happens below.)
          break;
        }
        throw new NumberFormatException(LazyUtils.convertToString(bytes, start,
            length));
      }
      if (max > result) {
        throw new NumberFormatException(LazyUtils.convertToString(bytes, start,
            length));
      }
      int next = result * radix - digit;
      if (next > result) {
        throw new NumberFormatException(LazyUtils.convertToString(bytes, start,
            length));
      }
      result = next;
    }
      /**
       * 上面的逻辑
       * 设置数字是25
       * 因为第一个数字是2
       * 因此变成-2
       * 第二个数字是5.因此变成-20-(5) = -25
       */

    // This is the case when we've encountered a decimal separator. The fractional
    // part will not change the number, but we will verify that the fractional part
    // is well formed.
    while (offset < end) {
      int digit = LazyUtils.digit(bytes[offset++], radix);
      if (digit == -1) {
        throw new NumberFormatException(LazyUtils.convertToString(bytes, start,
            length));
      }
    }

    if (!negative) {//进来说明是正数
      result = -result;//去负数,因此-25就变成25
      if (result < 0) {
        throw new NumberFormatException(LazyUtils.convertToString(bytes, start,
            length));
      }
    }
    return result;
  }

  /**
   * Writes out the text representation of an integer using base 10 to an
   * OutputStream in UTF-8 encoding.
   * 
   * Note: division by a constant (like 10) is much faster than division by a
   * variable. That's one of the reasons that we don't make radix a parameter
   * here.
   * 
   * @param out
   *          the outputstream to write to
   * @param i
   *          an int to write out
   * @throws IOException
   * 如何将i序列化
   * 该算法耗费很多字节,比如存储20001,要使用5个字节
   */
  public static void writeUTF8(OutputStream out, int i) throws IOException {
    if (i == 0) {
      out.write('0');
      return;
    }

    boolean negative = i < 0;
    if (negative) {
      out.write('-');
    } else {
      // negative range is bigger than positive range, so there is no risk
      // of overflow here.
      i = -i;
    }

    int start = 1000000000;//int最大值的位数
    while (i / start == 0) {//如果i=12,则只需要10即可
      start /= 10;
    }

    while (start > 0) {
      out.write('0' - (i / start % 10));//分别计算i的每一位内容,一定是负数,因此与0处理,变成整数
      start /= 10;
    }
  }

  //如何将i序列化
  public static void writeUTF8NoException(OutputStream out, int i) {
    try {
      writeUTF8(out, i);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
