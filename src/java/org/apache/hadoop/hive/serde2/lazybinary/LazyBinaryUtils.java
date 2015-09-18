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
package org.apache.hadoop.hive.serde2.lazybinary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.serde2.ByteStream.Output;
import org.apache.hadoop.hive.serde2.io.TimestampWritable;
import org.apache.hadoop.hive.serde2.lazybinary.objectinspector.LazyBinaryObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.ListTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.MapTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.StructTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.WritableUtils;

/**
 * LazyBinaryUtils.
 *
 */
public final class LazyBinaryUtils {

  private static Log LOG = LogFactory.getLog(LazyBinaryUtils.class.getName());

  /**
   * Convert the byte array to an int starting from the given offset. Refer to
   * code by aeden on DZone Snippets:
   *
   * @param b
   *          the byte array
   * @param offset
   *          the array offset
   * @return the integer
   * 从字节数组中获取4个字节组装成int
   */
  public static int byteArrayToInt(byte[] b, int offset) {
    int value = 0;
    for (int i = 0; i < 4; i++) {
      int shift = (4 - 1 - i) * 8;
      value += (b[i + offset] & 0x000000FF) << shift;
    }
    return value;
  }

  /**
   * Convert the byte array to a long starting from the given offset.
   *
   * @param b
   *          the byte array
   * @param offset
   *          the array offset
   * @return the long
   * 从字节数组中获取8个字节组装成long
   */
  public static long byteArrayToLong(byte[] b, int offset) {
    long value = 0;
    for (int i = 0; i < 8; i++) {
      int shift = (8 - 1 - i) * 8;
      value += ((long) (b[i + offset] & 0x00000000000000FF)) << shift;
    }
    return value;
  }

  /**
   * Convert the byte array to a short starting from the given offset.
   *
   * @param b
   *          the byte array
   * @param offset
   *          the array offset
   * @return the short
   * 从字节数组中获取2个字节组装成short
   */
  public static short byteArrayToShort(byte[] b, int offset) {
    short value = 0;
    value += (b[offset] & 0x000000FF) << 8;
    value += (b[offset + 1] & 0x000000FF);
    return value;
  }

  /**
   * Record is the unit that data is serialized in. A record includes two parts.
   * The first part stores the size of the element and the second part stores
   * the real element. size element record -> |----|-------------------------|
   *
   * A RecordInfo stores two information of a record, the size of the "size"
   * part which is the element offset and the size of the element part which is
   * element size.
   */
  public static class RecordInfo {
    public RecordInfo() {
      elementOffset = 0;
      elementSize = 0;
    }

    public byte elementOffset;
    public int elementSize;

    @Override
    public String toString() {
      return "(" + elementOffset + ", " + elementSize + ")";
    }
  }

  private static ThreadLocal<VInt> vIntThreadLocal = new ThreadLocal<VInt>() {
    @Override
    public VInt initialValue() {
      return new VInt();
    }
  };

  /**
   * Check a particular field and set its size and offset in bytes based on the
   * field type and the bytes arrays.
   *
   * For void, boolean, byte, short, int, long, float and double, there is no
   * offset and the size is fixed. For string, map, list, struct, the first four
   * bytes are used to store the size. So the offset is 4 and the size is
   * computed by concating the first four bytes together. The first four bytes
   * are defined with respect to the offset in the bytes arrays.
   * For timestamp, if the first bit is 0, the record length is 4, otherwise
   * a VInt begins at the 5th byte and its length is added to 4.
   *
   * @param objectInspector
   *          object inspector of the field
   * @param bytes
   *          bytes arrays store the table row
   * @param offset
   *          offset of this field
   * @param recordInfo
   *          modify this byteinfo object and return it
   * 根据参数objectInspector不同类型,设置recordInfo对象值,根据不同类型,设置需要多少个字节
   */
  public static void checkObjectByteInfo(ObjectInspector objectInspector,
      byte[] bytes, int offset, RecordInfo recordInfo) {
    VInt vInt = vIntThreadLocal.get();
    Category category = objectInspector.getCategory();
    switch (category) {
    case PRIMITIVE:
      PrimitiveCategory primitiveCategory = ((PrimitiveObjectInspector) objectInspector)
          .getPrimitiveCategory();
      switch (primitiveCategory) {
      case VOID:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = 0;
        break;
      case BOOLEAN:
      case BYTE:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = 1;
        break;
      case SHORT:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = 2;
        break;
      case FLOAT:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = 4;
        break;
      case DOUBLE:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = 8;
        break;
      case INT:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = WritableUtils.decodeVIntSize(bytes[offset]);
        break;
      case LONG:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = WritableUtils.decodeVIntSize(bytes[offset]);
        break;
      case STRING:
        // using vint instead of 4 bytes
        LazyBinaryUtils.readVInt(bytes, offset, vInt);
        recordInfo.elementOffset = vInt.length;
        recordInfo.elementSize = vInt.value;
        break;

      case VARCHAR:
        LazyBinaryUtils.readVInt(bytes, offset, vInt);
        recordInfo.elementOffset = vInt.length;
        recordInfo.elementSize = vInt.value;
        break;
      case BINARY:
        // using vint instead of 4 bytes
        LazyBinaryUtils.readVInt(bytes, offset, vInt);
        recordInfo.elementOffset = vInt.length;
        recordInfo.elementSize = vInt.value;
        break;
      case DATE:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = WritableUtils.decodeVIntSize(bytes[offset]);
        break;
      case TIMESTAMP:
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = TimestampWritable.getTotalLength(bytes, offset);
        break;
      case DECIMAL:
        // using vint instead of 4 bytes
        LazyBinaryUtils.readVInt(bytes, offset, vInt);
        recordInfo.elementOffset = 0;
        recordInfo.elementSize = vInt.length;
        LazyBinaryUtils.readVInt(bytes, offset + vInt.length, vInt);
        recordInfo.elementSize += vInt.length + vInt.value;
        break;
      default: {
        throw new RuntimeException("Unrecognized primitive type: "
            + primitiveCategory);
      }
      }
      break;
    case LIST:
    case MAP:
    case STRUCT:
      recordInfo.elementOffset = 4;
      recordInfo.elementSize = LazyBinaryUtils.byteArrayToInt(bytes, offset);//返回元素数量
      break;
    default: {
      throw new RuntimeException("Unrecognized non-primitive type: " + category);
    }
    }
  }

  /**
   * A zero-compressed encoded long.
   * 记录可变的long,最终long的值和long所占字节大小存储到该对象中
   */
  public static class VLong {
    public VLong() {
      value = 0;
      length = 0;
    }

    public long value;//该变长的long的value值
    public byte length;//该可变长long所占用字节长度
  };

  /**
   * Reads a zero-compressed encoded long from a byte array and returns it.
   *
   * @param bytes
   *          the byte array
   * @param offset
   *          offset of the array to read from
   * @param vlong
   *          storing the deserialized long and its size in byte
   * 读取变长的long对象,将值存储到VLong对象中
   */
  public static void readVLong(byte[] bytes, int offset, VLong vlong) {
    byte firstByte = bytes[offset];
    vlong.length = (byte) WritableUtils.decodeVIntSize(firstByte);//读取变长的long的应有长度
    if (vlong.length == 1) {
      vlong.value = firstByte;
      return;
    }
    long i = 0;
    for (int idx = 0; idx < vlong.length - 1; idx++) {
      byte b = bytes[offset + 1 + idx];
      i = i << 8;
      i = i | (b & 0xFF);
    }
    vlong.value = (WritableUtils.isNegativeVInt(firstByte) ? (i ^ -1L) : i);
  }

  /**
   * A zero-compressed encoded integer.
   * 记录可变的int,最终int的值和int所占字节大小存储到该对象中
   */
  public static class VInt {
    public VInt() {
      value = 0;
      length = 0;
    }

    public int value;
    public byte length;
  };

  public static final ThreadLocal<VInt> threadLocalVInt = new ThreadLocal<VInt>() {
    @Override
    protected VInt initialValue() {
      return new VInt();
    }
  };

  /**
   * Reads a zero-compressed encoded int from a byte array and returns it.
   *
   * @param bytes
   *          the byte array
   * @param offset
   *          offset of the array to read from
   * @param vInt
   *          storing the deserialized int and its size in byte
   *  读取变长的int对象,将值存储到VInt对象中 
   */
  public static void readVInt(byte[] bytes, int offset, VInt vInt) {
    byte firstByte = bytes[offset];
    vInt.length = (byte) WritableUtils.decodeVIntSize(firstByte);//需要占用的字节大小
    if (vInt.length == 1) {
      vInt.value = firstByte;
      return;
    }
    int i = 0;
    for (int idx = 0; idx < vInt.length - 1; idx++) {
      byte b = bytes[offset + 1 + idx];
      i = i << 8;
      i = i | (b & 0xFF);
    }
    vInt.value = (WritableUtils.isNegativeVInt(firstByte) ? (i ^ -1) : i);
  }

  /**
   * Writes a zero-compressed encoded int to a byte array.
   *
   * @param byteStream
   *          the byte array/stream
   * @param i
   *          the int
   * 将int值变成可变字节数组,存储到输出流中         
   */
  public static void writeVInt(Output byteStream, int i) {
    writeVLong(byteStream, i);
  }

  /**
   * Read a zero-compressed encoded long from a byte array.
   *
   * @param bytes the byte array
   * @param offset the offset in the byte array where the VLong is stored
   * @return the long
   * 从字节数组的offset中读取一个可变字节数组组成的long值返回
   */
  public static long readVLongFromByteArray(final byte[] bytes, int offset) {
    byte firstByte = bytes[offset++];
    int len = WritableUtils.decodeVIntSize(firstByte);
    if (len == 1) {
      return firstByte;
    }
    long i = 0;
    for (int idx = 0; idx < len-1; idx++) {
      byte b = bytes[offset++];
      i = i << 8;
      i = i | (b & 0xFF);
    }
    return (WritableUtils.isNegativeVInt(firstByte) ? ~i : i);
  }

  /**
   * Write a zero-compressed encoded long to a byte array.
   *
   * @param bytes
   *          the byte array/stream
   * @param l
   *          the long
   * 将long的值存储在bytes数组中,从0位置开始存储
   * @return 返回一共占用多少个字节存储该long
   */
  public static int writeVLongToByteArray(byte[] bytes, long l) {
    return LazyBinaryUtils.writeVLongToByteArray(bytes, 0, l);
  }

  /**
   * 将long的值存储在bytes数组中,从offset位置开始存储
   * @return 返回一共占用多少个字节存储该long
   */
  public static int writeVLongToByteArray(byte[] bytes, int offset, long l) {
    if (l >= -112 && l <= 127) {//用一个字节就可以存储long了
      bytes[offset] = (byte) l;
      return 1;
    }

    int len = -112;
    if (l < 0) {
      l ^= -1L; // take one's complement'
      len = -120;
    }

    long tmp = l;
    while (tmp != 0) {
      tmp = tmp >> 8;
      len--;
    }

    bytes[offset] = (byte) len;

    len = (len < -120) ? -(len + 120) : -(len + 112);

    for (int idx = len; idx != 0; idx--) {
      int shiftbits = (idx - 1) * 8;
      long mask = 0xFFL << shiftbits;
      bytes[offset+1-(idx - len)] = (byte) ((l & mask) >> shiftbits);
    }
    return 1 + len;
  }

  //每一个线程缓存一个字节数组,用于复用,该字节数组中存储随后要使用的long的字节信息
  private static ThreadLocal<byte[]> vLongBytesThreadLocal = new ThreadLocal<byte[]>() {
    @Override
    public byte[] initialValue() {
      return new byte[9];
    }
  };

  //将long类型的值,使用可变数组存储起来,并且将其输出到输出流中
  public static void writeVLong(Output byteStream, long l) {
    byte[] vLongBytes = vLongBytesThreadLocal.get();
    int len = LazyBinaryUtils.writeVLongToByteArray(vLongBytes, l);
    byteStream.write(vLongBytes, 0, len);
  }

  //缓存
  static HashMap<TypeInfo, ObjectInspector> cachedLazyBinaryObjectInspector = new HashMap<TypeInfo, ObjectInspector>();

  /**
   * Returns the lazy binary object inspector that can be used to inspect an
   * lazy binary object of that typeInfo
   *
   * For primitive types, we use the standard writable object inspector.
   */
  public static ObjectInspector getLazyBinaryObjectInspectorFromTypeInfo(
      TypeInfo typeInfo) {
    ObjectInspector result = cachedLazyBinaryObjectInspector.get(typeInfo);
    if (result == null) {
      switch (typeInfo.getCategory()) {
      case PRIMITIVE: {
        result = PrimitiveObjectInspectorFactory
            .getPrimitiveWritableObjectInspector(((PrimitiveTypeInfo) typeInfo));
        break;
      }
      case LIST: {
        ObjectInspector elementObjectInspector = getLazyBinaryObjectInspectorFromTypeInfo(((ListTypeInfo) typeInfo)
            .getListElementTypeInfo());
        result = LazyBinaryObjectInspectorFactory
            .getLazyBinaryListObjectInspector(elementObjectInspector);
        break;
      }
      case MAP: {
        MapTypeInfo mapTypeInfo = (MapTypeInfo) typeInfo;
        ObjectInspector keyObjectInspector = getLazyBinaryObjectInspectorFromTypeInfo(mapTypeInfo
            .getMapKeyTypeInfo());
        ObjectInspector valueObjectInspector = getLazyBinaryObjectInspectorFromTypeInfo(mapTypeInfo
            .getMapValueTypeInfo());
        result = LazyBinaryObjectInspectorFactory
            .getLazyBinaryMapObjectInspector(keyObjectInspector,
            valueObjectInspector);
        break;
      }
      case STRUCT: {
        StructTypeInfo structTypeInfo = (StructTypeInfo) typeInfo;
        List<String> fieldNames = structTypeInfo.getAllStructFieldNames();
        List<TypeInfo> fieldTypeInfos = structTypeInfo
            .getAllStructFieldTypeInfos();
        List<ObjectInspector> fieldObjectInspectors = new ArrayList<ObjectInspector>(
            fieldTypeInfos.size());
        for (int i = 0; i < fieldTypeInfos.size(); i++) {
          fieldObjectInspectors
              .add(getLazyBinaryObjectInspectorFromTypeInfo(fieldTypeInfos
              .get(i)));
        }
        result = LazyBinaryObjectInspectorFactory
            .getLazyBinaryStructObjectInspector(fieldNames,
            fieldObjectInspectors);
        break;
      }
      default: {
        result = null;
      }
      }
      cachedLazyBinaryObjectInspector.put(typeInfo, result);
    }
    return result;
  }

  private LazyBinaryUtils() {
    // prevent instantiation
  }
}
