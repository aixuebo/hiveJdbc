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
package org.apache.hadoop.hive.serde2.lazy;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hadoop.hive.serde2.lazy.objectinspector.LazyMapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.io.Text;

/**
 * LazyMap stores a map of Primitive LazyObjects to LazyObjects. Note that the
 * keys of the map cannot contain null.
 *
 * LazyMap does not deal with the case of a NULL map. That is handled by the
 * parent LazyObject.
 * 
 * 懒加载,只有需要的时候才真正意义的去解析数据
 * 解析Map,key是基础类型,value是任意类型,key-value类型一定是初始化的时候就固定好的
 */
public class LazyMap extends LazyNonPrimitive<LazyMapObjectInspector> {

  /**
   * Whether the data is already parsed or not.
   * true表示解析完成
   */
  boolean parsed = false;

  /**
   * The size of the map. Only valid when the data is parsed. -1 when the map is
   * NULL.
   * map中key-value的键值对数量
   */
  int mapSize = 0;

  /**
   * The beginning position of key[i]. Only valid when the data is parsed. Note
   * that keyStart[mapSize] = begin + length + 1; that makes sure we can use the
   * same formula to compute the length of each value in the map.
   * 存储每一个key-value的开始位置,连续两个位置之差,就是一个key-value所占用的字节总数
   */
  int[] keyStart;

  /**
   * The end position of key[i] (the position of the key-value separator). Only
   * valid when the data is parsed.
   * 存储每一个key-value中key的结束位置
   */
  int[] keyEnd;
  /**
   * The keys are stored in an array of LazyPrimitives.
   * 存储keyInited为true时,存储对应的下标对应的key的对象
   */
  LazyPrimitive<?, ?>[] keyObjects;
  /**
   * Whether init() is called on keyObjects[i].
   * key所在的下标如果为true,表示该key已经被初始化了
   */
  boolean[] keyInited;
  /**
   * The values are stored in an array of LazyObjects. value[index] will start
   * from KeyEnd[index] + 1, and ends before KeyStart[index+1] - 1.
   * 存储valueInited为true时,存储对应的下标对应的value的对象
   */
  LazyObject[] valueObjects;
  /**
   * Whether init() is called on valueObjects[i].
   * value所在的下标如果为true,表示该value已经被初始化了
   */
  boolean[] valueInited;

  /**
   * Construct a LazyMap object with the ObjectInspector.
   */
  protected LazyMap(LazyMapObjectInspector oi) {
    super(oi);
  }

  /**
   * Set the row data for this LazyArray.
   *
   * @see LazyObject#init(ByteArrayRef, int, int)
   * 初始化数据,将数据传入,但是不去解析
   */
  @Override
  public void init(ByteArrayRef bytes, int start, int length) {
    super.init(bytes, start, length);
    parsed = false;
    cachedMap = null;
  }

  /**
   * Enlarge the size of arrays storing information for the elements inside the
   * array.
   * 扩容数组
   */
  protected void enlargeArrays() {
    if (keyStart == null) {
      int initialSize = 2;
      keyStart = new int[initialSize];
      keyEnd = new int[initialSize];
      keyObjects = new LazyPrimitive<?, ?>[initialSize];
      valueObjects = new LazyObject[initialSize];
      keyInited = new boolean[initialSize];
      valueInited = new boolean[initialSize];
    } else {
      keyStart = Arrays.copyOf(keyStart, keyStart.length * 2);
      keyEnd = Arrays.copyOf(keyEnd, keyEnd.length * 2);
      keyObjects = Arrays.copyOf(keyObjects, keyObjects.length * 2);
      valueObjects = Arrays.copyOf(valueObjects, valueObjects.length * 2);
      keyInited = Arrays.copyOf(keyInited, keyInited.length * 2);
      valueInited = Arrays.copyOf(valueInited, valueInited.length * 2);
    }
  }

  /**
   * Parse the byte[] and fill keyStart, keyEnd.
   * 解析字节数组
   */
  private void parse() {
    parsed = true;

    byte itemSeparator = oi.getItemSeparator();//每个元素的分隔符
    byte keyValueSeparator = oi.getKeyValueSeparator();//map中key-value的分隔符
    boolean isEscaped = oi.isEscaped();
    byte escapeChar = oi.getEscapeChar();

    // empty array?
    if (length == 0) {
      mapSize = 0;
      return;
    }

    mapSize = 0;
    int arrayByteEnd = start + length;//最终字节结尾
    int elementByteBegin = start;//当前处理的位置
    int keyValueSeparatorPosition = -1;//当前处理的key-value的分隔符位置
    int elementByteEnd = start;
    byte[] bytes = this.bytes.getData();

    // Go through all bytes in the byte[]
    while (elementByteEnd <= arrayByteEnd) {
      // End of entry reached?
      if (elementByteEnd == arrayByteEnd
          || bytes[elementByteEnd] == itemSeparator) {//是最后或者是元素分隔符了,则进行如下处理
        // Array full?扩容数组
        if (keyStart == null || mapSize + 1 == keyStart.length) {
          enlargeArrays();
        }
        keyStart[mapSize] = elementByteBegin;
        // If no keyValueSeparator is seen, all bytes belong to key, and
        // value will be NULL.
        keyEnd[mapSize] = (keyValueSeparatorPosition == -1 ? elementByteEnd
            : keyValueSeparatorPosition);
        // reset keyValueSeparatorPosition
        keyValueSeparatorPosition = -1;
        mapSize++;
        elementByteBegin = elementByteEnd + 1;
        elementByteEnd++;
      } else {
        // Is this the first keyValueSeparator in this entry?
        if (keyValueSeparatorPosition == -1
            && bytes[elementByteEnd] == keyValueSeparator) {//设置key-value中key的结束位置
          keyValueSeparatorPosition = elementByteEnd;
        }
        if (isEscaped && bytes[elementByteEnd] == escapeChar
            && elementByteEnd + 1 < arrayByteEnd) {//遇见转义字符,则将转义字符的下一个字符也一起跳跃过去,不需要被解析了
          // ignore the char after escape_char
          elementByteEnd += 2;
        } else {
          elementByteEnd++;
        }
      }
    }

    // This makes sure we can use the same formula to compute the
    // length of each value in the map.
    keyStart[mapSize] = arrayByteEnd + 1;

    //都设置为false
    if (mapSize > 0) {
      Arrays.fill(keyInited, 0, mapSize, false);
      Arrays.fill(valueInited, 0, mapSize, false);
    }
  }

  /**
   * Get the value in the map for the key.
   *
   * If there are multiple matches (which is possible in the serialized format),
   * only the first one is returned.
   *
   * The most efficient way to get the value for the key is to serialize the key
   * and then try to find it in the array. We do linear search because in most
   * cases, user only wants to get one or two values out of the map, and the
   * cost of building up a HashMap is substantially higher.
   *
   * @param key
   *          The key object that we are looking for.
   * @return The corresponding value object, or NULL if not found
   * 获取map中存储的key对应的value值
   */
  public Object getMapValueElement(Object key) {
    if (!parsed) {
      parse();
    }
    // search for the key
    for (int i = 0; i < mapSize; i++) {
      //获取key
      LazyPrimitive<?, ?> lazyKeyI = uncheckedGetKey(i);
      if (lazyKeyI == null) {//说明该key不存在
        continue;
      }
      // getWritableObject() will convert LazyPrimitive to actual primitive
      // writable objects.
      //获取key对应的值,注意获取的是可序列化的
      Object keyI = lazyKeyI.getWritableObject();
      if (keyI == null) {
        continue;
      }
      
      //如果key对应的值与参数相同,则获取对应的value
      if (keyI.equals(key)) {
        // Got a match, return the value
        LazyObject v = uncheckedGetValue(i);
        return v == null ? v : v.getObject();
      }
    }

    return null;
  }

  /**
   * Get the value object with the index without checking parsed.
   *
   * @param index
   *          The index into the array starting from 0
   * 获取第index位置的value值         
   */
  private LazyObject uncheckedGetValue(int index) {
    if (valueInited[index]) {//如果被初始化了,则直接获取
      return valueObjects[index];
    }
    
    //设置value已经被初始化了
    valueInited[index] = true;
    Text nullSequence = oi.getNullSequence();
    int valueIBegin = keyEnd[index] + 1;
    int valueILength = keyStart[index + 1] - 1 - valueIBegin;
    if (valueILength < 0
        || ((valueILength == nullSequence.getLength()) && 0 == LazyUtils
        .compare(bytes.getData(), valueIBegin, valueILength, nullSequence
        .getBytes(), 0, nullSequence.getLength()))) {//如果value的值与nullSequence相同,则设置value为null
      return valueObjects[index] = null;
    }
    //创建value对象,并且调用初始化方法
    valueObjects[index] = LazyFactory
        .createLazyObject(oi.getMapValueObjectInspector());
    valueObjects[index].init(bytes, valueIBegin, valueILength);
    return valueObjects[index];
  }

  /**
   * Get the key object with the index without checking parsed.
   *
   * @param index
   *          The index into the array starting from 0
   * 获取第index个对应的key         
   */
  private LazyPrimitive<?, ?> uncheckedGetKey(int index) {
	  //如果该key已经被加载过了,则直接过去即可
    if (keyInited[index]) {
      return keyObjects[index];
    }
    
    //设置该key被加载了
    keyInited[index] = true;

    Text nullSequence = oi.getNullSequence();
    int keyIBegin = keyStart[index];//key的起始位置
    int keyILength = keyEnd[index] - keyStart[index];//key所占字节长度
    if (keyILength < 0
        || ((keyILength == nullSequence.getLength()) && 0 == LazyUtils.compare(
        bytes.getData(), keyIBegin, keyILength, nullSequence.getBytes(), 0,
        nullSequence.getLength()))) {//如果key的信息与nullSequence相同,则设置key为null
      return keyObjects[index] = null;
    }
    //初始化该key,并且调用该key的init方法
    // Keys are always primitive
    keyObjects[index] = LazyFactory
        .createLazyPrimitiveClass((PrimitiveObjectInspector) oi.getMapKeyObjectInspector());
    keyObjects[index].init(bytes, keyIBegin, keyILength);
    return keyObjects[index];
  }

  /**
   * cachedMap is reused for different calls to getMap(). But each LazyMap has a
   * separate cachedMap so we won't overwrite the data by accident.
   */
  protected LinkedHashMap<Object, Object> cachedMap;

  /**
   * Return the map object representing this LazyMap. Note that the keyObjects
   * will be Writable primitive objects.
   *
   * @return the map object
   * 获取key-value对象的集合,并且缓存起来
   */
  public Map<Object, Object> getMap() {
    if (!parsed) {
      parse();
    }
    if (cachedMap != null) {
      return cachedMap;
    }
    // Use LinkedHashMap to provide deterministic order
    cachedMap = new LinkedHashMap<Object, Object>();

    // go through each element of the map
    for (int i = 0; i < mapSize; i++) {
      LazyPrimitive<?, ?> lazyKey = uncheckedGetKey(i);
      if (lazyKey == null) {
        continue;
      }
      Object key = lazyKey.getObject();
      // do not overwrite if there are duplicate keys
      if (key != null && !cachedMap.containsKey(key)) {
        LazyObject lazyValue = uncheckedGetValue(i);
        Object value = (lazyValue == null ? null : lazyValue.getObject());
        cachedMap.put(key, value);
      }
    }
    return cachedMap;
  }

  /**
   * Get the size of the map represented by this LazyMap.
   *
   * @return The size of the map, -1 for NULL map.
   * 获取map存放的元素数量
   */
  public int getMapSize() {
    if (!parsed) {
      parse();
    }
    return mapSize;
  }

  protected boolean getParsed() {
    return parsed;
  }

  protected void setParsed(boolean parsed) {
    this.parsed = parsed;
  }
}
