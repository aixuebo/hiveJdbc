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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.serde2.SerDeStatsStruct;
import org.apache.hadoop.hive.serde2.lazy.objectinspector.LazySimpleStructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.Text;

/**
 * LazyObject for storing a struct. The field of a struct can be primitive or
 * non-primitive.
 *
 * LazyStruct does not deal with the case of a NULL struct. That is handled by
 * the parent LazyObject.
 * 懒加载,当需要某一个属性的时候才去加载该属性,其他属性暂时不加载
 */
public class LazyStruct extends LazyNonPrimitive<LazySimpleStructObjectInspector> implements
    SerDeStatsStruct {

  private static Log LOG = LogFactory.getLog(LazyStruct.class.getName());

  /**
   * Whether the data is already parsed or not.
   * true表示已经解析好了
   */
  boolean parsed;

  /**
   * Size of serialized data
   * 该数据结构对应的字节数
   */
  long serializedSize;

  /**
   * The start positions of struct fields. Only valid when the data is parsed.
   * Note that startPosition[arrayLength] = begin + length + 1; that makes sure
   * we can use the same formula to compute the length of each element of the
   * array.
   * 每一个属性的开始字节位置,两个属性之间就是每一个属性所占用的字节长度
   */
  int[] startPosition;

  /**
   * The fields of the struct.
   * 所有的属性对应对象集合
   */
  LazyObject[] fields;
  /**
   * Whether init() has been called on the field or not.
   * 判断哪些属性已经被初始化了
   */
  boolean[] fieldInited;

  /**
   * Construct a LazyStruct object with the ObjectInspector.
   */
  public LazyStruct(LazySimpleStructObjectInspector oi) {
    super(oi);
  }

  /**
   * Set the row data for this LazyStruct.
   *
   * @see LazyObject#init(ByteArrayRef, int, int)
   */
  @Override
  public void init(ByteArrayRef bytes, int start, int length) {
    super.init(bytes, start, length);
    parsed = false;
    serializedSize = length;
  }

  boolean missingFieldWarned = false;//true表示有属性缺失,即按照顺序执行时候发现字节数组内容无法满足所有属性赋值,忽略缺失的属性
  boolean extraFieldWarned = false;//true表示被检测到还有额外的字节

  /**
   * Parse the byte[] and fill each field.
   * 解析每一个属性对应的开始字节位置,但是尚未真的解析
   */
  private void parse() {

    byte separator = oi.getSeparator();
    boolean lastColumnTakesRest = oi.getLastColumnTakesRest();
    boolean isEscaped = oi.isEscaped();
    byte escapeChar = oi.getEscapeChar();

    if (fields == null) {
      List<? extends StructField> fieldRefs = ((StructObjectInspector) oi)
          .getAllStructFieldRefs();//获取所有的属性集合
      fields = new LazyObject[fieldRefs.size()];
      for (int i = 0; i < fields.length; i++) {//为每一个属性创建对应的对象
        fields[i] = LazyFactory.createLazyObject(fieldRefs.get(i)
            .getFieldObjectInspector());
      }
      fieldInited = new boolean[fields.length];
      // Extra element to make sure we have the same formula to compute the
      // length of each element of the array.
      startPosition = new int[fields.length + 1];
    }

    int structByteEnd = start + length;//字节数组的结尾最后一个字节位置
    int fieldId = 0;
    int fieldByteBegin = start;
    int fieldByteEnd = start;
    byte[] bytes = this.bytes.getData();

    // Go through all bytes in the byte[]
    while (fieldByteEnd <= structByteEnd) {
      if (fieldByteEnd == structByteEnd || bytes[fieldByteEnd] == separator) {//需要进行拆分了
        // Reached the end of a field?
    	  /**
字段意义说明
CREATE TABLE escape2 (id STRING, name STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '"'; 

LOAD DATA LOCAL INPATH '/home/tianzhao/book/escape2.txt' 
OVERWRITE INTO TABLE escape2; 

escape2.txt 的内容是： 
Joe"2"3333"44 
Hank"2"3333"44 
实际数据比表的字段要多。 

select * from escape2; 
Joe 2 
Hank 2 
ALTER TABLE escape2 SET SERDEPROPERTIES ('serialization.last.column.takes.rest' = 'true'); 
serialization.last.column.takes.rest 的意思是最后一个字段的内容是否包含那些多余的数据： 
select * from escape2; 
Joe 2"3333"44 
Hank 2"3333"44 
    	   */
        if (lastColumnTakesRest && fieldId == fields.length - 1) {//最后一个属性的时候,值将不再进行拆分,直接到最后一个字节
          fieldByteEnd = structByteEnd;
        }
        startPosition[fieldId] = fieldByteBegin;
        fieldId++;
        if (fieldId == fields.length || fieldByteEnd == structByteEnd) {
          // All fields have been parsed, or bytes have been parsed.
        	//所有的属性都已经解析完了,或者字节已经没有可用的了
          // We need to set the startPosition of fields.length to ensure we
          // can use the same formula to calculate the length of each field.
          // For missing fields, their starting positions will all be the same,
          // which will make their lengths to be -1 and uncheckedGetField will
          // return these fields as NULLs.
        	//我们需要设置剩余的属性的开始位置,以确保公式可以正常访问,不出bug.
        	//对于缺失的属性,我们设置开始位置都一样,这样最后uncheckedGetField方法获取属性的值值就是null
          for (int i = fieldId; i <= fields.length; i++) {
            startPosition[i] = fieldByteEnd + 1;
          }
          break;
        }
        fieldByteBegin = fieldByteEnd + 1;
        fieldByteEnd++;
      } else {
        if (isEscaped && bytes[fieldByteEnd] == escapeChar
            && fieldByteEnd + 1 < structByteEnd) {//跳过转义字符后面的字符
          // ignore the char after escape_char
          fieldByteEnd += 2;
        } else {
          fieldByteEnd++;
        }
      }
    }

    // Extra bytes at the end?被检测到还有额外的字节,已经忽略了
    if (!extraFieldWarned && fieldByteEnd < structByteEnd) {
      extraFieldWarned = true;
      LOG.warn("Extra bytes detected at the end of the row! Ignoring similar "
          + "problems.");
    }

    // Missing fields?
    //有属性缺失,即按照顺序执行时候发现字节数组内容无法满足所有属性赋值,忽略缺失的属性
    if (!missingFieldWarned && fieldId < fields.length) {
      missingFieldWarned = true;
      LOG.info("Missing fields! Expected " + fields.length + " fields but "
          + "only got " + fieldId + "! Ignoring similar problems.");
    }

    Arrays.fill(fieldInited, false);
    parsed = true;
  }

  /**
   * Get one field out of the struct.
   *
   * If the field is a primitive field, return the actual object. Otherwise
   * return the LazyObject. This is because PrimitiveObjectInspector does not
   * have control over the object used by the user - the user simply directly
   * use the Object instead of going through Object
   * PrimitiveObjectInspector.get(Object).
   *
   * @param fieldID
   *          The field ID
   * @return The field as a LazyObject
   * 获取第index个属性对应的值
   */
  public Object getField(int fieldID) {
    if (!parsed) {
      parse();
    }
    return uncheckedGetField(fieldID);
  }

  /**
   * Get the field out of the row without checking parsed. This is called by
   * both getField and getFieldsAsList.
   *
   * @param fieldID
   *          The id of the field starting from 0.
   * @param nullSequence
   *          The sequence representing NULL value.
   * @return The value of the field
   * 获取第index个属性对应的值
   */
  private Object uncheckedGetField(int fieldID) {
    Text nullSequence = oi.getNullSequence();//先获取空的字符串内容
    // Test the length first so in most cases we avoid doing a byte[]
    // comparison.
    int fieldByteBegin = startPosition[fieldID];
    int fieldLength = startPosition[fieldID + 1] - startPosition[fieldID] - 1;//该属性对应的字节长度
    if ((fieldLength < 0)
        || (fieldLength == nullSequence.getLength() && LazyUtils.compare(bytes
            .getData(), fieldByteBegin, fieldLength, nullSequence.getBytes(),
            0, nullSequence.getLength()) == 0)) {//如果该属性对应的值的内容与nullSequence相同,则被设置为null
      return null;
    }
    //对属性的值进行初始化
    if (!fieldInited[fieldID]) {
      fieldInited[fieldID] = true;//说明该属性已经初始化完成
      fields[fieldID].init(bytes, fieldByteBegin, fieldLength);//真正的去还原一个数据
    }
    return fields[fieldID].getObject();
  }

  ArrayList<Object> cachedList;

  /**
   * Get the values of the fields as an ArrayList.
   *
   * @return The values of the fields as an ArrayList.
   * 获取所有属性对应的值的集合,应该没有被缓存
   */
  public ArrayList<Object> getFieldsAsList() {
    if (!parsed) {
      parse();
    }
    if (cachedList == null) {
      cachedList = new ArrayList<Object>();
    } else {
      cachedList.clear();
    }
    for (int i = 0; i < fields.length; i++) {
      cachedList.add(uncheckedGetField(i));
    }
    return cachedList;
  }

  @Override
  public Object getObject() {
    return this;
  }

  protected boolean getParsed() {
    return parsed;
  }

  protected void setParsed(boolean parsed) {
    this.parsed = parsed;
  }

  protected LazyObject[] getFields() {
    return fields;
  }

  protected void setFields(LazyObject[] fields) {
    this.fields = fields;
  }

  protected boolean[] getFieldInited() {
    return fieldInited;
  }

  protected void setFieldInited(boolean[] fieldInited) {
    this.fieldInited = fieldInited;
  }

  public long getRawDataSerializedSize() {
    return serializedSize;
  }
}
