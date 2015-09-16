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

package org.apache.hadoop.hive.serde2.lazy.objectinspector;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.serde2.lazy.LazyMap;
import org.apache.hadoop.hive.serde2.objectinspector.MapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.io.Text;

/**
 * LazyMapObjectInspector works on struct data that is stored in LazyStruct.
 * 
 * Always use the ObjectInspectorFactory to create new ObjectInspector objects,
 * instead of directly creating an instance of this class.
 * ����Map����,key��valueһ���ǹ̶�����������
 * ����Category.MAP����
 */
public class LazyMapObjectInspector implements MapObjectInspector {

  public static final Log LOG = LogFactory.getLog(LazyMapObjectInspector.class
      .getName());

  ObjectInspector mapKeyObjectInspector;//key�Ķ�������
  ObjectInspector mapValueObjectInspector;//value�Ķ�������

  byte itemSeparator;//ÿһ��key-value�ķָ���
  byte keyValueSeparator;//key-value֮��ķָ���
  Text nullSequence;//value����key��nullֵ,��key����value��nullSequence��ͬʱ,key/value�������ó�null
  boolean escaped;//�Ƿ���Ҫת��
  /**
   * ��Ҫת����ַ�,���escaped=true,������escapeChar��ʱ��,��escapeChar+֮����ַ���ԭ�������,����escapeChar=\ ������12\3��ʱ��,�����12\3
   * ������escapeChar=\,separator=,
   * ����:12\,45,555
   * ���:12\,45��555
   */
  byte escapeChar;

  /**
   * Call ObjectInspectorFactory.getStandardListObjectInspector instead.
   */
  protected LazyMapObjectInspector(ObjectInspector mapKeyObjectInspector,
      ObjectInspector mapValueObjectInspector, byte itemSeparator,
      byte keyValueSeparator, Text nullSequence, boolean escaped,
      byte escapeChar) {
    this.mapKeyObjectInspector = mapKeyObjectInspector;
    this.mapValueObjectInspector = mapValueObjectInspector;

    this.itemSeparator = itemSeparator;
    this.keyValueSeparator = keyValueSeparator;
    this.nullSequence = nullSequence;
    this.escaped = escaped;
    this.escapeChar = escapeChar;
  }

  @Override
  public final Category getCategory() {
    return Category.MAP;
  }

  //map<key������,value������>
  @Override
  public String getTypeName() {
    return org.apache.hadoop.hive.serde.serdeConstants.MAP_TYPE_NAME + "<"
        + mapKeyObjectInspector.getTypeName() + ","
        + mapValueObjectInspector.getTypeName() + ">";
  }

  @Override
  public ObjectInspector getMapKeyObjectInspector() {
    return mapKeyObjectInspector;
  }

  @Override
  public ObjectInspector getMapValueObjectInspector() {
    return mapValueObjectInspector;
  }

  /**
   * data��LazyMap����,��Ҫǿת,Ȼ���ȡkey��Ӧ��valueֵ
   */
  @Override
  public Object getMapValueElement(Object data, Object key) {
    if (data == null) {
      return null;
    }
    return ((LazyMap) data).getMapValueElement(key);
  }

  //data��LazyMap����,��Ҫǿת,���ظ�map���󼴿�
  @Override
  public Map<?, ?> getMap(Object data) {
    if (data == null) {
      return null;
    }
    return ((LazyMap) data).getMap();
  }

  //data��LazyMap����,��Ҫǿת,��ȡmap��ŵ�Ԫ������
  @Override
  public int getMapSize(Object data) {
    if (data == null) {
      return -1;
    }
    return ((LazyMap) data).getMapSize();
  }

  // Called by LazyMap
  public byte getItemSeparator() {
    return itemSeparator;
  }

  public byte getKeyValueSeparator() {
    return keyValueSeparator;
  }

  public Text getNullSequence() {
    return nullSequence;
  }

  public boolean isEscaped() {
    return escaped;
  }

  public byte getEscapeChar() {
    return escapeChar;
  }
}
