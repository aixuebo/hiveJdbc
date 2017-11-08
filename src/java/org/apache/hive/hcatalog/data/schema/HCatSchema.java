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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.hive.hcatalog.data.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.common.classification.InterfaceAudience;
import org.apache.hadoop.hive.common.classification.InterfaceStability;
import org.apache.hive.hcatalog.common.HCatException;

/**
 * HCatSchema. This class is NOT thread-safe.
 * 定义一个对象---通过序号/name两种方式找到该对象的一个属性
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class HCatSchema implements Serializable {

  private static final long serialVersionUID = 1L;

  private final List<HCatFieldSchema> fieldSchemas;//该对象有若干个属性---可以通过序号定位一个属性
  //HCatFieldSchema.getName()->position
  private final Map<String, Integer> fieldPositionMap;//该属性的name和对应的序号映射----通过name获取属性的序号,然后通过上面的集合映射,获取属性对象
  private final List<String> fieldNames;//属性名字集合

  /**
   *
   * @param fieldSchemas is now owned by HCatSchema. Any subsequent modifications
   * on fieldSchemas won't get reflected in HCatSchema.  Each fieldSchema's name
   * in the list must be unique, otherwise throws IllegalArgumentException.
   */
  public HCatSchema(final List<HCatFieldSchema> fieldSchemas) {
    this.fieldSchemas = new ArrayList<HCatFieldSchema>(fieldSchemas);
    int idx = 0;
    fieldPositionMap = new HashMap<String, Integer>();
    fieldNames = new ArrayList<String>();
    for (HCatFieldSchema field : fieldSchemas) {
      if (field == null)
        throw new IllegalArgumentException("Field cannot be null");

      String fieldName = normalizeName(field.getName());
      if (fieldPositionMap.containsKey(fieldName))
        throw new IllegalArgumentException("Field named " + field.getName() +
          " already exists");
      fieldPositionMap.put(fieldName, idx);
      fieldNames.add(fieldName);
      idx++;
    }
  }

  //追加一个属性
  public void append(final HCatFieldSchema hfs) throws HCatException {
    if (hfs == null)
      throw new HCatException("Attempt to append null HCatFieldSchema in HCatSchema.");

    String fieldName = normalizeName(hfs.getName());
    if (fieldPositionMap.containsKey(fieldName))
      throw new HCatException("Attempt to append HCatFieldSchema with already " +
        "existing name: " + fieldName + ".");

    this.fieldSchemas.add(hfs);
    this.fieldNames.add(fieldName);
    this.fieldPositionMap.put(fieldName, this.size() - 1);
  }

  /**
   *  Users are not allowed to modify the list directly, since HCatSchema
   *  maintains internal state. Use append/remove to modify the schema.
   *  获取该对象的所有属性
   */
  public List<HCatFieldSchema> getFields() {
    return Collections.unmodifiableList(this.fieldSchemas);
  }

  /**
   * Note : The position will be re-numbered when one of the preceding columns are removed.
   * Hence, the client should not cache this value and expect it to be always valid.
   * @param fieldName
   * @return the index of field named fieldName in Schema. If field is not
   * present, returns null.
   * 获取该属性的序号
   */
  public Integer getPosition(String fieldName) {
    return fieldPositionMap.get(normalizeName(fieldName));
  }

  //获取该属性对象
  public HCatFieldSchema get(String fieldName) throws HCatException {
    return get(getPosition(fieldName));
  }

  public List<String> getFieldNames() {
    return this.fieldNames;
  }

  //通过位置获取一个属性
  public HCatFieldSchema get(int position) {
    return fieldSchemas.get(position);
  }

  //有多少个属性
  public int size() {
    return fieldSchemas.size();
  }

  //因为删除一个属性后,属性的序号要有变化
    //参数是该属性的位置,该位置之后的属性都要变化.一般第二个参数都是-1,说明每一个序号都要-1
  private void reAlignPositionMap(int startPosition, int offset) {
    for (Map.Entry<String, Integer> entry : fieldPositionMap.entrySet()) {//循环每一个属性
      // Re-align the columns appearing on or after startPostion(say, column 1) such that
      // column 2 becomes column (2+offset), column 3 becomes column (3+offset) and so on.
      Integer entryVal = entry.getValue();
      if (entryVal >= startPosition) {
        entry.setValue(entryVal+offset);
      }
    }
  }

  //删除一个属性
  public void remove(final HCatFieldSchema hcatFieldSchema) throws HCatException {
    if (!fieldSchemas.contains(hcatFieldSchema)) {
      throw new HCatException("Attempt to delete a non-existent column from HCat Schema: " + hcatFieldSchema);
    }     
    fieldSchemas.remove(hcatFieldSchema);
    // Re-align the positionMap by -1 for the columns appearing after hcatFieldSchema.
    String fieldName = normalizeName(hcatFieldSchema.getName());
    reAlignPositionMap(fieldPositionMap.get(fieldName)+1, -1);
    fieldPositionMap.remove(fieldName);
    fieldNames.remove(fieldName);
  }

  private String normalizeName(String name) {
    return name == null ? null : name.toLowerCase();
  }

  //打印对象每一个属性的详细信息
  //demo:fieldName:属性.toString,fieldName:.toString,fieldName:.toString
  @Override
  public String toString() {
    boolean first = true;
    StringBuilder sb = new StringBuilder();
    for (HCatFieldSchema hfs : fieldSchemas) {
      if (!first) {
        sb.append(",");
      } else {
        first = false;
      }
      if (hfs.getName() != null) {
        sb.append(hfs.getName());
        sb.append(":");
      }
      sb.append(hfs.toString());
    }
    return sb.toString();
  }

  //打印对象的所有属性信息--即每一个属性Name和对应的类型
  //格式 fieldName:fieldType,fieldName:fieldType,fieldName:fieldType
  public String getSchemaAsTypeString() {
    boolean first = true;
    StringBuilder sb = new StringBuilder();
    for (HCatFieldSchema hfs : fieldSchemas) {
      if (!first) {
        sb.append(",");
      } else {
        first = false;
      }
      if (hfs.getName() != null) {
        sb.append(hfs.getName());
        sb.append(":");
      }
      sb.append(hfs.getTypeString());
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof HCatSchema)) {
      return false;
    }
    HCatSchema other = (HCatSchema) obj;
    if (!this.getFields().equals(other.getFields())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
