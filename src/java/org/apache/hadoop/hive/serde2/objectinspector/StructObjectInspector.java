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

import java.util.List;

/**
 * StructObjectInspector.
 *
 */
public abstract class StructObjectInspector implements ObjectInspector {

  // ** Methods that does not need a data object **
  /**
   * Returns all the fields.
   * 存储该对象所有的属性集合
   */
  public abstract List<? extends StructField> getAllStructFieldRefs();

  /**
   * Look up a field.
   * 将fieldName对应的fields对象获取出来
   * 其中参数fieldName可能是属性名字,也可能是属性下标
   */
  public abstract StructField getStructFieldRef(String fieldName);

  // ** Methods that need a data object **
  /**
   * returns null for data = null.
   * 获取某一个属性的值,其中data是LazyStruct对象,通过待获取属性值的属性index下标,获取第index个属性的值
   */
  public abstract Object getStructFieldData(Object data, StructField fieldRef);

  /**
   * returns null for data = null.
   * 获取该struct对象的每一个属性对应的值集合,集合的顺序是属性的顺序
   */
  public abstract List<Object> getStructFieldsDataAsList(Object data);

  public boolean isSettable() {
    return false;
  }

  /**
   * 打印该对象,以及该对象每一个属性的对象类型信息
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    List<? extends StructField> fields = getAllStructFieldRefs();
    sb.append(getClass().getName());
    sb.append("<");
    for (int i = 0; i < fields.size(); i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(fields.get(i).getFieldObjectInspector().toString());
    }
    sb.append(">");
    return sb.toString();
  }
}
