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

import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;

/**
 * Interface to encapsulate retrieving of type information, for the object inspector factory.
 * 基础类型的额外接口,包含基础类型的具体分类、如果类型有参数,要描述参数类型
 */
public interface PrimitiveTypeSpec {
  /**
   * @return  PrimitiveCategory referred to by the PrimitiveTypeSpec
   * 该接触类型的详细分类
   */
  PrimitiveCategory getPrimitiveCategory();

  /**
   * @return Type params referred to by the PrimitiveTypeSpec
   * 需要获取该基础类型的参数对象
   * 如果类型有参数,要描述参数类型,例如char(10) or decimal(10, 2).
   */
  BaseTypeParams getTypeParams();
}
