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
 * 定义Map对象,key和value一定是固定的两个对象
 * 属于Category.MAP分类
 */
public class LazyMapObjectInspector implements MapObjectInspector {

  public static final Log LOG = LogFactory.getLog(LazyMapObjectInspector.class
      .getName());

  ObjectInspector mapKeyObjectInspector;//key的对象类型
  ObjectInspector mapValueObjectInspector;//value的对象类型

  byte itemSeparator;//每一组key-value的分隔符
  byte keyValueSeparator;//key-value之间的分隔符
  Text nullSequence;//value或者key的null值,当key或者value与nullSequence相同时,key/value将被设置成null
  boolean escaped;//是否需要转义
  /**
   * 需要转义的字符,如果escaped=true,则当遇见escapeChar的时候,则将escapeChar+之后的字符都原样的输出,比如escapeChar=\ 则当遇见12\3的时候,会输出12\3
   * 再例如escapeChar=\,separator=,
   * 输入:12\,45,555
   * 输出:12\,45和555
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

  //map<key的类型,value的类型>
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
   * data是LazyMap类型,需要强转,然后获取key对应的value值
   */
  @Override
  public Object getMapValueElement(Object data, Object key) {
    if (data == null) {
      return null;
    }
    return ((LazyMap) data).getMapValueElement(key);
  }

  //data是LazyMap类型,需要强转,返回该map对象即可
  @Override
  public Map<?, ?> getMap(Object data) {
    if (data == null) {
      return null;
    }
    return ((LazyMap) data).getMap();
  }

  //data是LazyMap类型,需要强转,获取map存放的元素数量
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
