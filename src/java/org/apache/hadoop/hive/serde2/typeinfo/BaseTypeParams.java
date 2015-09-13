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

import java.io.Serializable;

import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.io.Writable;

/**
 * Base type for type-specific params, such as char(10) or decimal(10, 2).
 * 类型基数类,可以设置类型的参数
 * 该对象实现了hadoop的序列化、java的序列化
 */
public abstract class BaseTypeParams implements Writable, Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 校验
   * 出问题就抛异常即可
   */
  public abstract void validateParams() throws SerDeException;

  /**
   * char(10) or decimal(10, 2).
   * 解析参数,因为char(10)这个参数只能是一个,就是属性长度
   * decimal有两个参数
   */
  public abstract void populateParams(String[] params) throws SerDeException;

  public abstract String toString();

  //先解析参数,然后校验
  public void set(String[] params) throws SerDeException {
    populateParams(params);
    validateParams();
  }

  // Needed for conversion to/from TypeQualifiers. Override in subclasses.
  //是否有最大字符限制
  public boolean hasCharacterMaximumLength() {
    return false;
  }
  
  //如果有最大字符限制,则限制字符是多少,例如char(10),最多允许输入10个字符
  public int getCharacterMaximumLength() {
    return -1;
  }
}
