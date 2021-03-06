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

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils.PrimitiveTypeEntry;

/**
 * There are limited number of Primitive Types. All Primitive Types are defined
 * by TypeInfoFactory.isPrimitiveClass().
 *
 * Always use the TypeInfoFactory to create new TypeInfo objects, instead of
 * directly creating an instance of this class.
 * 基础类型,包含基础类型的参数对象
 *
 * List、map、union、struct都是由原始类型组成的,因此这几个种类的TypeInfo实现比较简单,只需要递归即可
 * 原始类型的TypeInfo就比较复杂了，包括typeName(即设计数据库时候指定的,比如decimal(10, 2)) 参数类型(10, 2))
 * 因此有了typeName,就可以获取该原始类型对应的小分类,即decimal,以及对该类java如何序列化,hadoop如何序列化等都可以查找到
 */
public class PrimitiveTypeInfo extends TypeInfo implements Serializable, PrimitiveTypeSpec {

  private static final long serialVersionUID = 1L;

  protected String typeName;//基础类型字符串名称--比如decimal 不带有参数信息
  protected BaseTypeParams typeParams;//基础类型的参数,比如字符串类型的需要设置最大长度

  /**
   * For java serialization use only.
   */
  public PrimitiveTypeInfo() {
  }

  /**
   * For TypeInfoFactory use only.
   */
  PrimitiveTypeInfo(String typeName) {
    this.typeName = typeName;
  }

  /**
   * Returns the category of this TypeInfo.
   */
  @Override
  public Category getCategory() {
    return Category.PRIMITIVE;
  }

    //通过具体的原始类型,可以得到原始类型分类对象
  public PrimitiveCategory getPrimitiveCategory() {
    return getPrimitiveTypeEntry().primitiveCategory;
  }

  //基础类型hadoop的序列化类
  public Class<?> getPrimitiveWritableClass() {
    return PrimitiveObjectInspectorUtils.getTypeEntryFromTypeName(typeName).primitiveWritableClass;
  }

  //基础类型java的序列化类
  public Class<?> getPrimitiveJavaClass() {
    return PrimitiveObjectInspectorUtils.getTypeEntryFromTypeName(typeName).primitiveJavaClass;
  }

  // The following 2 methods are for java serialization use only.
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  @Override
  public String getTypeName() {
    return typeName;
  }

  /**
   * If the type has type parameters (such as varchar length, or decimal precision/scale),
   * then return the parameters for the type.
   * @return A BaseTypeParams object representing the parameters for the type, or null
   * 获取该基础类型的参数,比如字符长度等信息
   */
  public BaseTypeParams getTypeParams() {
    return typeParams;
  }

  /**
   * Set the type parameters for the type.
   * @param typeParams type parameters for the type
   * 设置基础类型的参数
   */
  public void setTypeParams(BaseTypeParams typeParams) {
    // Ideally could check here to make sure the type really supports parameters,
    // however during deserialization some of the required fields are not set at the
    // time that the type params are set. We would have to customize the way this class
    // is serialized/deserialized for the check to work.
    //if (typeParams != null && !getPrimitiveTypeEntry().isParameterized()) {
    //  throw new UnsupportedOperationException(
    //      "Attempting to add type parameters " + typeParams + " to type " + getTypeName());
    //}
    this.typeParams = typeParams;
  }

  public PrimitiveTypeEntry getPrimitiveTypeEntry() {
    return PrimitiveObjectInspectorUtils.getTypeEntryFromTypeName(
        TypeInfoUtils.getBaseName(typeName));
  }

  /**
   * Compare if 2 TypeInfos are the same. We use TypeInfoFactory to cache
   * TypeInfos, so we only need to compare the Object pointer.
   */
  @Override
  public boolean equals(Object other) {
    return this == other;
  }

  /**
   * Generate the hashCode for this TypeInfo.
   */
  @Override
  public int hashCode() {
    return typeName.hashCode();
  }

  @Override
  public String toString() {
    return typeName;
  }
}
