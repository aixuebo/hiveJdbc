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
 * ����,һ������ֻ�ܴ洢һ��Ԫ������
 */
public class LazyArray extends LazyNonPrimitive<LazyListObjectInspector> {

  /**
   * Whether the data is already parsed or not.
   * �Ƿ�������
   */
  boolean parsed = false;
  /**
   * The length of the array. Only valid when the data is parsed. -1 when the
   * array is NULL.
   * ����洢Ԫ�صĸ���
   */
  int arrayLength = 0;

  /**
   * The start positions of array elements. Only valid when the data is parsed.
   * Note that startPosition[arrayLength] = begin + length + 1; that makes sure
   * we can use the same formula to compute the length of each element of the
   * array.
   * ������ÿһ��Ԫ�صĵ�һ���ַ���byte�����е�λ��,
   * ͨ�����ڵ�����Ԫ�ص�һ���ַ�����λ������,�Ϳ��Ի�ȡĳһ��Ԫ����ӵ�е�ȫ���ֽ�����
   */
  int[] startPosition;

  /**
   * Whether init() has been called on the element or not.
   * �ж�ÿһ�������Ƿ񱻳�ʼ����,��size��arrayElements��startPosition��size��һ�µ�
   */
  boolean[] elementInited;

  /**
   * The elements of the array. Note that we do arrayElements[i]. init(bytes,
   * begin, length) only when that element is accessed.
   * �洢ÿһ��Ԫ�ض���
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
	  //�����ֽ�����,���ǲ����н���
    super.init(bytes, start, length);
    parsed = false;
    cachedList = null;
  }

  /**
   * Enlarge the size of arrays storing information for the elements inside the
   * array.
   * ����
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
    boolean isEscaped = oi.isEscaped();//�Ƿ���Ҫת��
    byte escapeChar = oi.getEscapeChar();//ת���ַ�

    // empty array?
    if (length == 0) {
      arrayLength = 0;
      return;
    }

    byte[] bytes = this.bytes.getData();//�ȴ�������ֽ�����

    arrayLength = 0;
    int arrayByteEnd = start + length;//������ֽ����һ��λ��
    int elementByteBegin = start;//�����ڴ����Ԫ�صĿ�ʼλ��
    int elementByteEnd = start;//�����ڴ����Ԫ�صĽ���λ��

    // Go through all bytes in the byte[]
    while (elementByteEnd <= arrayByteEnd) {//ֻҪû���ֽڵ����λ�þͽ���ѭ��
      // Reached the end of a field?
      if (elementByteEnd == arrayByteEnd || bytes[elementByteEnd] == separator) {//�����ǰ�ֽ������һ���ֽڻ����ǲ���ֽ�,�����if
        // Array size not big enough?
        if (startPosition == null || arrayLength + 1 == startPosition.length) {//����Ϊnull,����������С������
          enlargeArrays();//����
        }
        startPosition[arrayLength] = elementByteBegin;
        arrayLength++;
        //������һ��Ԫ�صĿ�ʼλ�úͽ���λ��
        elementByteBegin = elementByteEnd + 1;
        elementByteEnd++;
      } else {//�����в��.���ۼӽ���λ�ü���,����ѭ��������һ���ַ�
    	  /**
    	   * ��Ҫת����ַ�,���escaped=true,������escapeChar��ʱ��,��escapeChar+֮����ַ���ԭ�������,����escapeChar=\ ������12\3��ʱ��,�����12\3
    	   * ������escapeChar=\,separator=,
    	   * ����:12\,45,555
    	   * ���:12\,45��555
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
   * ��ȡ��indexλ�õ�Ԫ�ض���
   */
  public Object getListElementObject(int index) {
    if (!parsed) {//���û����,��Ҫ�Ƚ���
      parse();
    }
    if (index < 0 || index >= arrayLength) {//У��indexλ���Ƿ���ȷ
      return null;
    }
    return uncheckedGetElement(index);
  }

  /**
   * Get the element without checking out-of-bound index.
   * ��ȡ��indexλ�õ�Ԫ�ض���
   */
  private Object uncheckedGetElement(int index) {
    if (elementInited[index]) {//˵���Ѿ���ʼ����,��ֱ�ӷ��ظ�Ԫ�ض�Ӧ�Ķ��󼴿�
      return arrayElements[index] == null ? null : arrayElements[index].getObject();
    }
    //�����Ѿ���ʼ����
    elementInited[index] = true;

    //����������ʵ������Ԫ��
    Text nullSequence = oi.getNullSequence();

    //ͨ�����ڵ�����Ԫ�ص�һ���ַ�����λ������,�Ϳ��Ի�ȡĳһ��Ԫ����ӵ�е�ȫ���ֽ�����
    int elementLength = startPosition[index + 1] - startPosition[index] - 1;
    if (elementLength == nullSequence.getLength()
        && 0 == LazyUtils
        .compare(bytes.getData(), startPosition[index], elementLength,
        nullSequence.getBytes(), 0, nullSequence.getLength())) {//�жϴ��ֽ������л�ȡ���ַ���Ϣ��nullSequence��Ϣһ��
    	//������ø�ֵΪnull
      return arrayElements[index] = null;
    }
    //������Ԫ�ش���ڸ�Ԫ��λ��
    arrayElements[index] = LazyFactory
        .createLazyObject(oi.getListElementObjectInspector());
    //�Ը�Ԫ�ؽ��г�ʼ��
    arrayElements[index].init(bytes, startPosition[index], elementLength);
    return arrayElements[index].getObject();
  }

  /**
   * Returns -1 for null array.
   * ����Ԫ�ظ���
   */
  public int getListLength() {
    if (!parsed) {//���û����,����н���
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
   * ����Ԫ�ؼ���.������ӻ���
   */
  public List<Object> getList() {
    if (!parsed) {//���û����,����н���
      parse();
    }
    if (arrayLength == -1) {
      return null;
    }
    if (cachedList != null) {//��ȡ��������
      return cachedList;
    }
    //��ӻ�������
    cachedList = new ArrayList<Object>(arrayLength);
    for (int index = 0; index < arrayLength; index++) {
      cachedList.add(uncheckedGetElement(index));
    }
    return cachedList;
  }
}
