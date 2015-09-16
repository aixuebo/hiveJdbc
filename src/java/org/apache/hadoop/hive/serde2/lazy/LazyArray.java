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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.hive.serde2.lazy.objectinspector.LazyListObjectInspector;
import org.apache.hadoop.io.Text;

/**
 * LazyArray stores an array of Lazy Objects.
 *
 * LazyArray does not deal with the case of a NULL array. That is handled by the
 * parent LazyObject.
 * 数组,一个数组只能存储一种元素类型
 */
public class LazyArray extends LazyNonPrimitive<LazyListObjectInspector> {

  /**
   * Whether the data is already parsed or not.
   * 是否解析完成
   */
  boolean parsed = false;
  /**
   * The length of the array. Only valid when the data is parsed. -1 when the
   * array is NULL.
   * 数组存储元素的个数
   */
  int arrayLength = 0;

  /**
   * The start positions of array elements. Only valid when the data is parsed.
   * Note that startPosition[arrayLength] = begin + length + 1; that makes sure
   * we can use the same formula to compute the length of each element of the
   * array.
   * 数组中每一个元素的第一个字符在byte数组中的位置,
   * 通过相邻的两个元素第一个字符所在位置做差,就可以获取某一个元素所拥有的全部字节数组
   */
  int[] startPosition;

  /**
   * Whether init() has been called on the element or not.
   * 判断每一个对象是否被初始化了,该size与arrayElements和startPosition的size是一致的
   */
  boolean[] elementInited;

  /**
   * The elements of the array. Note that we do arrayElements[i]. init(bytes,
   * begin, length) only when that element is accessed.
   * 存储每一个元素对象
   */
  LazyObject[] arrayElements;

  /**
   * Construct a LazyArray object with the ObjectInspector.
   *
   * @param oi
   *          the oi representing the type of this LazyArray as well as meta
   *          information like separator etc.
   */
  protected LazyArray(LazyListObjectInspector oi) {
    super(oi);
  }

  /**
   * Set the row data for this LazyArray.
   *
   * @see LazyObject#init(ByteArrayRef, int, int)
   */
  @Override
  public void init(ByteArrayRef bytes, int start, int length) {
	  //设置字节数组,但是不进行解析
    super.init(bytes, start, length);
    parsed = false;
    cachedList = null;
  }

  /**
   * Enlarge the size of arrays storing information for the elements inside the
   * array.
   * 扩容
   */
  private void enlargeArrays() {
    if (startPosition == null) {
      int initialSize = 2;
      startPosition = new int[initialSize];
      arrayElements = new LazyObject[initialSize];
      elementInited = new boolean[initialSize];
    } else {
      startPosition = Arrays.copyOf(startPosition, startPosition.length * 2);
      arrayElements = Arrays.copyOf(arrayElements, arrayElements.length * 2);
      elementInited = Arrays.copyOf(elementInited, elementInited.length * 2);
    }
  }

  /**
   * Parse the bytes and fill arrayLength and startPosition.
   */
  private void parse() {
    parsed = true;

    byte separator = oi.getSeparator();
    boolean isEscaped = oi.isEscaped();//是否需要转义
    byte escapeChar = oi.getEscapeChar();//转义字符

    // empty array?
    if (length == 0) {
      arrayLength = 0;
      return;
    }

    byte[] bytes = this.bytes.getData();//等待处理的字节数组

    arrayLength = 0;
    int arrayByteEnd = start + length;//处理的字节最后一个位置
    int elementByteBegin = start;//现在在处理的元素的开始位置
    int elementByteEnd = start;//现在在处理的元素的结束位置

    // Go through all bytes in the byte[]
    while (elementByteEnd <= arrayByteEnd) {//只要没到字节的最后位置就进行循环
      // Reached the end of a field?
      if (elementByteEnd == arrayByteEnd || bytes[elementByteEnd] == separator) {//如果当前字节是最后一个字节或者是拆分字节,则进入if
        // Array size not big enough?
        if (startPosition == null || arrayLength + 1 == startPosition.length) {//容器为null,或者容器大小不够了
          enlargeArrays();//扩容
        }
        startPosition[arrayLength] = elementByteBegin;
        arrayLength++;
        //设置下一个元素的开始位置和结束位置
        elementByteBegin = elementByteEnd + 1;
        elementByteEnd++;
      } else {//不进行拆分.则累加结束位置即可,继续循环处理下一个字符
    	  /**
    	   * 需要转义的字符,如果escaped=true,则当遇见escapeChar的时候,则将escapeChar+之后的字符都原样的输出,比如escapeChar=\ 则当遇见12\3的时候,会输出12\3
    	   * 再例如escapeChar=\,separator=,
    	   * 输入:12\,45,555
    	   * 输出:12\,45和555
    	   */
        if (isEscaped && bytes[elementByteEnd] == escapeChar
            && elementByteEnd + 1 < arrayByteEnd) {
          // ignore the char after escape_char
          elementByteEnd += 2;
        } else {
          elementByteEnd++;
        }
      }
    }
    // Store arrayByteEnd+1 in startPosition[arrayLength]
    // so that we can use the same formula to compute the length of
    // each element in the array: startPosition[i+1] - startPosition[i] - 1
    startPosition[arrayLength] = arrayByteEnd + 1;

    if (arrayLength > 0) {
      Arrays.fill(elementInited, 0, arrayLength, false);
    }

  }

  /**
   * Returns the actual primitive object at the index position inside the array
   * represented by this LazyObject.
   * 获取第index位置的元素对象
   */
  public Object getListElementObject(int index) {
    if (!parsed) {//如果没解析,则要先解析
      parse();
    }
    if (index < 0 || index >= arrayLength) {//校验index位置是否正确
      return null;
    }
    return uncheckedGetElement(index);
  }

  /**
   * Get the element without checking out-of-bound index.
   * 获取第index位置的元素对象
   */
  private Object uncheckedGetElement(int index) {
    if (elementInited[index]) {//说明已经初始化了,则直接返回该元素对应的对象即可
      return arrayElements[index] == null ? null : arrayElements[index].getObject();
    }
    //设置已经初始化了
    elementInited[index] = true;

    //进行真正的实例化该元素
    Text nullSequence = oi.getNullSequence();

    //通过相邻的两个元素第一个字符所在位置做差,就可以获取某一个元素所拥有的全部字节数组
    int elementLength = startPosition[index + 1] - startPosition[index] - 1;
    if (elementLength == nullSequence.getLength()
        && 0 == LazyUtils
        .compare(bytes.getData(), startPosition[index], elementLength,
        nullSequence.getBytes(), 0, nullSequence.getLength())) {//判断从字节数组中获取的字符信息与nullSequence信息一样
    	//因此设置该值为null
      return arrayElements[index] = null;
    }
    //创建新元素存放在该元素位置
    arrayElements[index] = LazyFactory
        .createLazyObject(oi.getListElementObjectInspector());
    //对该元素进行初始化
    arrayElements[index].init(bytes, startPosition[index], elementLength);
    return arrayElements[index].getObject();
  }

  /**
   * Returns -1 for null array.
   * 数组元素个数
   */
  public int getListLength() {
    if (!parsed) {//如果没解析,则进行解析
      parse();
    }
    return arrayLength;
  }

  /**
   * cachedList is reused every time getList is called. Different LazyArray
   * instances cannot share the same cachedList.
   */
  ArrayList<Object> cachedList;

  /**
   * Returns the List of actual primitive objects. Returns null for null array.
   * 返回元素集合.并且添加缓存
   */
  public List<Object> getList() {
    if (!parsed) {//如果没解析,则进行解析
      parse();
    }
    if (arrayLength == -1) {
      return null;
    }
    if (cachedList != null) {//获取缓存数据
      return cachedList;
    }
    //添加缓存数据
    cachedList = new ArrayList<Object>(arrayLength);
    for (int index = 0; index < arrayLength; index++) {
      cachedList.add(uncheckedGetElement(index));
    }
    return cachedList;
  }
}
