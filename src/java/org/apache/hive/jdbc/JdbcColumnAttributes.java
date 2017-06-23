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

package org.apache.hive.jdbc;

//表示一个列的特别属性信息,比如decimal属性需要除了设置类型外,还要设置精准度
class JdbcColumnAttributes {
  public int precision = 0;//该字段也存储verchar对应的字段长度
  public int scale = 0;

  public JdbcColumnAttributes() {
  }

  public JdbcColumnAttributes(int precision, int scale) {
    this.precision = precision;
    this.scale = scale;
  }
}