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
package org.apache.hadoop.hive.serde2.typeinfo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.hive.common.type.HiveVarchar;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.io.WritableUtils;

/**
 * 字符串类型需要的参数对象
 */
public class VarcharTypeParams extends BaseTypeParams implements Serializable {
  private static final long serialVersionUID = 1L;

  public int length;//要求的字符串长度

  /**
   * 校验,必须有字符长度.并且长度要小于65535
   */
  @Override
  public void validateParams() throws SerDeException {
    if (length < 1) {
      throw new SerDeException("VARCHAR length must be positive");
    }
    if (length > HiveVarchar.MAX_VARCHAR_LENGTH) {
      throw new SerDeException("Length " + length
          + " exceeds max varchar length of " + HiveVarchar.MAX_VARCHAR_LENGTH);
    }
  }

  /**
   * 解析参数,因为var(25)这个参数只能是一个,就是属性长度
   */
  @Override
  public void populateParams(String[] params) throws SerDeException {
    if (params.length != 1) {
      throw new SerDeException("Invalid number of parameters for VARCHAR");
    }
    try {
      length = Integer.valueOf(params[0]);
    } catch (NumberFormatException err) {
      throw new SerDeException("Error setting VARCHAR length: " + err);
    }
  }

    //仅仅表示参数部分的输出,即(length)
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("(");
    sb.append(length);
    sb.append(")");
    return sb.toString();
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    length = WritableUtils.readVInt(in);
    try {
      validateParams();
    } catch (SerDeException err) {
      throw new IOException(err);
    }
  }

  @Override
  public void write(DataOutput out) throws IOException {
    WritableUtils.writeVInt(out,  length);
  }

  public int getLength() {
    return length;
  }

  public void setLength(int len) {
    length = len;
  }

  //是否有最大字符限制
  @Override
  public boolean hasCharacterMaximumLength() {
    return true;
  }
  
  //如果有最大字符限制,则限制字符是多少,例如char(10),最多允许输入10个字符
  @Override
  public int getCharacterMaximumLength() {
    return length;
  }
}
