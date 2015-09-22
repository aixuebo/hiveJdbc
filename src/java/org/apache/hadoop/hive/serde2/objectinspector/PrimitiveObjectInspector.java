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
package org.apache.hadoop.hive.serde2.objectinspector;

import org.apache.hadoop.hive.serde2.typeinfo.BaseTypeParams;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeSpec;

/**
 * PrimitiveObjectInspector.
 * 基础类型的具体类型、以及参数的解析、
 * 以及java/hadoop的序列化对象class
 */
public interface PrimitiveObjectInspector extends ObjectInspector, PrimitiveTypeSpec {

  /**
   * The primitive types supported by Hive.
   */
  public static enum PrimitiveCategory {
    VOID, BOOLEAN, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING,
    DATE, TIMESTAMP, BINARY, DECIMAL, VARCHAR, UNKNOWN
  };

  /**
   * Get the primitive category of the PrimitiveObjectInspector.
   * 基础类型的具体分类
   */
  PrimitiveCategory getPrimitiveCategory();

  /**
   * Get the Primitive Writable class which is the return type of
   * getPrimitiveWritableObject() and copyToPrimitiveWritableObject().
   * 使用hadoop序列化类
   */
  Class<?> getPrimitiveWritableClass();

  /**
   * Return the data in an instance of primitive writable Object. If the Object
   * is already a primitive writable Object, just return o.
   * 使用hadoop序列化对象
   */
  Object getPrimitiveWritableObject(Object o);

  /**
   * Get the Java Primitive class which is the return type of
   * getJavaPrimitiveObject().
   * 使用java序列化进行的class对象
   */
  Class<?> getJavaPrimitiveClass();

  /**
   * Get the Java Primitive object.
   * 使用java序列化进行序列化
   */
  Object getPrimitiveJavaObject(Object o);

  /**
   * Get a copy of the Object in the same class, so the return value can be
   * stored independently of the parameter.
   *
   * If the Object is a Primitive Java Object, we just return the parameter
   * since Primitive Java Object is immutable.
   */
  Object copyObject(Object o);

  /**
   * Whether the ObjectInspector prefers to return a Primitive Writable Object
   * instead of a Primitive Java Object. This can be useful for determining the
   * most efficient way to getting data out of the Object.
   * true表示该原始对象可以支持hadoop的序列化,从而不需要进行java序列化
   * 字面含义表示更喜欢序列化表示形式
   */
  boolean preferWritable();

  /**
   * If the type has type parameters (such as varchar length, or decimal precision/scale),
   * then return the parameters for the type.
   * @return A BaseTypeParams object representing the parameters for the type, or null
   * 如果类型有参数,要描述参数类型,例如char(10) or decimal(10, 2).
   */
  BaseTypeParams getTypeParams();

  /**
   * Set the type parameters for the type.
   * @param newParams type parameters for the type
   * 如果类型有参数,要描述参数类型,例如char(10) or decimal(10, 2).
   */
  void setTypeParams(BaseTypeParams newParams);
}
