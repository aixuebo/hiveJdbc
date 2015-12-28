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

package org.apache.hadoop.hive.ql.udf.generic;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.apache.hadoop.hive.ql.exec.FunctionRegistry;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.serde2.io.HiveVarcharWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters.Converter;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters.IdentityConverter;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory.ObjectInspectorOptions;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils.ObjectInspectorCopyOption;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.VoidObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.hive.serde2.typeinfo.VarcharTypeParams;
import org.apache.hadoop.io.Text;

/**
 * Util functions for GenericUDF classes.
 */
public final class GenericUDFUtils {
  /**
   * Checks if b is the first byte of a UTF-8 character.
   * 0xc0表示192,二进制是11000000
   * 0x80表示128,二进制是10000000
   * 检查是否是UTF-8编码
   */
  public static boolean isUtfStartByte(byte b) {
    return (b & 0xC0) != 0x80;
  }

  /**
   * This class helps to find the return ObjectInspector for a GenericUDF.
   * 
   * In many cases like CASE and IF, the GenericUDF is returning a value out of
   * several possibilities. However these possibilities may not always have the
   * same ObjectInspector.
   * 返回值可能有若干个可能性,然后这些可能性不能总是相同的对象
   * 
   * This class will help detect whether all possibilities have exactly the same
   * ObjectInspector. If not, then we need to convert the Objects to the same
   * ObjectInspector.
   * 
   * A special case is when some values are constant NULL. In this case we can
   * use the same ObjectInspector.
   * 
   * 该类是静态类
   */
  public static class ReturnObjectInspectorResolver {

    boolean allowTypeConversion;//是否允许类型转换
    ObjectInspector returnObjectInspector;//计算返回值类型

    // We create converters beforehand, so that the converters can reuse the
    // same object for returning conversion results.
    /**
new IdentityConverter()
new StructConverter
new ListConverter
new MapConverter
new PrimitiveObjectInspectorConverter
key是类型,value是该类型对应的转换器
     */
    HashMap<ObjectInspector, Converter> converters;

    public ReturnObjectInspectorResolver() {
      this(false);
    }

    public ReturnObjectInspectorResolver(boolean allowTypeConversion) {
      this.allowTypeConversion = allowTypeConversion;
    }

    /**
     * Update returnObjectInspector and valueInspectorsAreTheSame based on the
     * ObjectInspector seen.
     * 更新返回值类型
     * @return false if there is a type mismatch
     * false表示类型错误匹配
     */
    public boolean update(ObjectInspector oi) throws UDFArgumentTypeException {
      if (oi instanceof VoidObjectInspector) {
        return true;
      }

      if (returnObjectInspector == null) {
        // The first argument, just set the return to be the standard
        // writable version of this OI.
    	//计算返回值类型,该值是hadoop的序列化的返回值类型,而不是java的返回类型
        returnObjectInspector = ObjectInspectorUtils
            .getStandardObjectInspector(oi,
            ObjectInspectorCopyOption.WRITABLE);
        return true;
      }

      //传入的数据类型必须与初始化的返回类型相同,如果相同,则返回true
      if (returnObjectInspector == oi) {
        // The new ObjectInspector is the same as the old one, directly return
        // true
        return true;
      }

      TypeInfo oiTypeInfo = TypeInfoUtils.getTypeInfoFromObjectInspector(oi);
      TypeInfo rTypeInfo = TypeInfoUtils
          .getTypeInfoFromObjectInspector(returnObjectInspector);
      if (oiTypeInfo == rTypeInfo) {
        // Convert everything to writable, if types of arguments are the same,
        // but ObjectInspectors are different.
        returnObjectInspector = ObjectInspectorUtils
            .getStandardObjectInspector(returnObjectInspector,
            ObjectInspectorCopyOption.WRITABLE);
        return true;
      }

      if (!allowTypeConversion) {
        return false;
      }

      // Types are different, we need to check whether we can convert them to
      // a common base class or not.
      TypeInfo commonTypeInfo = FunctionRegistry.getCommonClass(oiTypeInfo,
          rTypeInfo);
      if (commonTypeInfo == null) {
        return false;
      }

      returnObjectInspector = TypeInfoUtils
          .getStandardWritableObjectInspectorFromTypeInfo(commonTypeInfo);

      return true;
    }

    /**
     * Returns the ObjectInspector of the return value.
     */
    public ObjectInspector get() {
      return returnObjectInspector;
    }

    /**
     * Convert the return Object if necessary (when the ObjectInspectors of
     * different possibilities are not all the same).
     * 当参数oi与返回值是不同类型时,需要对o进行转换
     */
    public Object convertIfNecessary(Object o, ObjectInspector oi) {
      Object converted = null;
      if (oi == returnObjectInspector) {
        converted = o;
      } else {

        if (o == null) {
          return null;
        }

        if (converters == null) {
          converters = new HashMap<ObjectInspector, Converter>();
        }

        Converter converter = converters.get(oi);
        if (converter == null) {
          converter = ObjectInspectorConverters.getConverter(oi,
              returnObjectInspector);
          converters.put(oi, converter);
        }
        converted = converter.convert(o);
      }
      return converted;
    }

  }

  /**
   * Convert parameters for the method if needed.
   */
  public static class ConversionHelper {

    private final ObjectInspector[] givenParameterOIs;//等待匹配的参数集合
    Type[] methodParameterTypes;//method方法的参数集合
    private final boolean isVariableLengthArgument;//是否最后一个参数是可变参数
    Type lastParaElementType;//最后一个参数类型

    boolean conversionNeeded;
    Converter[] converters;
    Object[] convertedParameters;
    Object[] convertedParametersInArray;

    private static Class<?> getClassFromType(Type t) {
      if (t instanceof Class<?>) {
        return (Class<?>) t;
      } else if (t instanceof ParameterizedType) {
        ParameterizedType pt = (ParameterizedType) t;
        return (Class<?>) pt.getRawType();
      }
      return null;
    }

    /**
     * Create a PrimitiveConversionHelper for Method m. The ObjectInspector's
     * input parameters are specified in parameters.
     */
    public ConversionHelper(Method m, ObjectInspector[] parameterOIs)
        throws UDFArgumentException {
      givenParameterOIs = parameterOIs;

      methodParameterTypes = m.getGenericParameterTypes();

      // Whether the method takes an array like Object[],
      // or String[] etc in the last argument.
      lastParaElementType = TypeInfoUtils
          .getArrayElementType(methodParameterTypes.length == 0 ? null
          : methodParameterTypes[methodParameterTypes.length - 1]);
      isVariableLengthArgument = (lastParaElementType != null);//是否最后一个参数是可变参数

      // Create the output OI array创建最终的输出参数类型集合
      ObjectInspector[] methodParameterOIs = new ObjectInspector[parameterOIs.length];

      if (isVariableLengthArgument) {//是可变参数

        // ConversionHelper can be called without method parameter length
        // checkings
        // for terminatePartial() and merge() calls.
        if (parameterOIs.length < methodParameterTypes.length - 1) {//参数提供的少了,因此抛异常
          throw new UDFArgumentLengthException(m.toString()
              + " requires at least " + (methodParameterTypes.length - 1)
              + " arguments but only " + parameterOIs.length
              + " are passed in.");
        }
        // Copy the first methodParameterTypes.length - 1 entries 先解析固定位置的参数
        for (int i = 0; i < methodParameterTypes.length - 1; i++) {
          // This method takes Object, so it accepts whatever types that are
          // passed in.
          if (methodParameterTypes[i] == Object.class) {
            methodParameterOIs[i] = ObjectInspectorUtils
                .getStandardObjectInspector(parameterOIs[i],
                ObjectInspectorCopyOption.JAVA);
          } else {
            methodParameterOIs[i] = ObjectInspectorFactory
                .getReflectionObjectInspector(methodParameterTypes[i],
                ObjectInspectorOptions.JAVA);
          }
        }

        // Deal with the last entry 解析最后一位置可变参数
        if (lastParaElementType == Object.class) {
          // This method takes Object[], so it accepts whatever types that are
          // passed in.
          for (int i = methodParameterTypes.length - 1; i < parameterOIs.length; i++) {
            methodParameterOIs[i] = ObjectInspectorUtils
                .getStandardObjectInspector(parameterOIs[i],
                ObjectInspectorCopyOption.JAVA);
          }
        } else {
          // This method takes something like String[], so it only accepts
          // something like String
          ObjectInspector oi = ObjectInspectorFactory
              .getReflectionObjectInspector(lastParaElementType,
              ObjectInspectorOptions.JAVA);
          for (int i = methodParameterTypes.length - 1; i < parameterOIs.length; i++) {
            methodParameterOIs[i] = oi;
          }
        }

      } else {//没有可变参数

        // Normal case, the last parameter is a normal parameter.
        // ConversionHelper can be called without method parameter length
        // checkings
        // for terminatePartial() and merge() calls.
        if (methodParameterTypes.length != parameterOIs.length) {//参数数量必须匹配成功
          throw new UDFArgumentLengthException(m.toString() + " requires "
              + methodParameterTypes.length + " arguments but "
              + parameterOIs.length + " are passed in.");
        }
        for (int i = 0; i < methodParameterTypes.length; i++) {
          // This method takes Object, so it accepts whatever types that are
          // passed in.
          if (methodParameterTypes[i] == Object.class) {
            methodParameterOIs[i] = ObjectInspectorUtils
                .getStandardObjectInspector(parameterOIs[i],
                ObjectInspectorCopyOption.JAVA);
          } else {
            methodParameterOIs[i] = ObjectInspectorFactory
                .getReflectionObjectInspector(methodParameterTypes[i],
                ObjectInspectorOptions.JAVA);
          }
        }
      }

      // Create the converters
      conversionNeeded = false;
      converters = new Converter[parameterOIs.length];
      for (int i = 0; i < parameterOIs.length; i++) {
        Converter pc = ObjectInspectorConverters.getConverter(parameterOIs[i],
            methodParameterOIs[i]);
        converters[i] = pc;
        // Conversion is needed?
        conversionNeeded = conversionNeeded
            || (!(pc instanceof IdentityConverter));
      }

      if (isVariableLengthArgument) {
        convertedParameters = new Object[methodParameterTypes.length];
        convertedParametersInArray = (Object[]) Array.newInstance(
            getClassFromType(lastParaElementType), parameterOIs.length
            - methodParameterTypes.length + 1);
        convertedParameters[convertedParameters.length - 1] = convertedParametersInArray;
      } else {
        convertedParameters = new Object[parameterOIs.length];
      }
    }

    public Object[] convertIfNecessary(Object... parameters) {

      assert (parameters.length == givenParameterOIs.length);

      if (!conversionNeeded && !isVariableLengthArgument) {
        // no conversion needed, and not variable-length argument:
        // just return what is passed in.
        return parameters;
      }

      if (isVariableLengthArgument) {
        // convert the first methodParameterTypes.length - 1 entries
        for (int i = 0; i < methodParameterTypes.length - 1; i++) {
          convertedParameters[i] = converters[i].convert(parameters[i]);
        }
        // convert the rest and put into the last entry
        for (int i = methodParameterTypes.length - 1; i < parameters.length; i++) {
          convertedParametersInArray[i + 1 - methodParameterTypes.length] = converters[i]
              .convert(parameters[i]);
        }
      } else {
        // normal case, convert all parameters
        for (int i = 0; i < methodParameterTypes.length; i++) {
          convertedParameters[i] = converters[i].convert(parameters[i]);
        }
      }
      return convertedParameters;
    }
  };

  /**
   * Helper class for UDFs returning string/varchar/char
   * 一个帮助类,返回值转变成string/varchar/char
   */
  public static class StringHelper {

    protected Object returnValue;
    protected PrimitiveCategory type;

    public StringHelper(PrimitiveCategory type) throws UDFArgumentException {
      this.type = type;
      switch (type) {
        case STRING:
          returnValue = new Text();
          break;
        case VARCHAR:
          returnValue = new HiveVarcharWritable();
          break;
        default:
          throw new UDFArgumentException("Unexpected non-string type " + type);
      }
    }

    public Object setReturnValue(String val) throws UDFArgumentException {
      if (val == null) {
        return null;
      }
      switch (type) {
        case STRING:
          ((Text)returnValue).set(val);
          return returnValue;
        case VARCHAR:
          ((HiveVarcharWritable)returnValue).set(val);
          return returnValue;
        default:
          throw new UDFArgumentException("Bad return type " + type);
      }
    }

    /**
     * Helper function to help GenericUDFs determine the return type
     * character length for char/varchar.
     * @param poi PrimitiveObjectInspector representing the type
     * @return character length of the type
     * @throws UDFArgumentException
     * 获取verchar类型的长度
     */
    public static int getFixedStringSizeForType(PrimitiveObjectInspector poi)
        throws UDFArgumentException {
      // TODO: we can support date, int, .. any types which would have a fixed length value
      switch (poi.getPrimitiveCategory()) {
        case VARCHAR:
          VarcharTypeParams varcharParams = null;
          varcharParams = (VarcharTypeParams) poi.getTypeParams();
          if (varcharParams == null || varcharParams.length < 0) {
            throw new UDFArgumentException("varchar type used without type params");
          }
          return varcharParams.length;
        default:
          throw new UDFArgumentException("No fixed size for type " + poi.getTypeName());
      }
    }

  }

  /**
   * Return an ordinal from an integer.
   */
  public static String getOrdinal(int i) {
    int unit = i % 10;
    return (i <= 0) ? "" : (i != 11 && unit == 1) ? i + "st"
        : (i != 12 && unit == 2) ? i + "nd" : (i != 13 && unit == 3) ? i + "rd"
        : i + "th";
  }

  /**
   * Finds any occurence of <code>subtext</code> from <code>text</code> in the
   * backing buffer, for avoiding string encoding and decoding. Shamelessly copy
   * from {@link org.apache.hadoop.io.Text#find(String, int)}.
   * 从Text中第start位置开始查找,找匹配subtext的第一个字符串位置
   */
  public static int findText(Text text, Text subtext, int start) {
    // src.position(start) can't accept negative numbers.
    if (start < 0) {
      return -1;
    }

    ByteBuffer src = ByteBuffer.wrap(text.getBytes(), 0, text.getLength());
    ByteBuffer tgt = ByteBuffer
        .wrap(subtext.getBytes(), 0, subtext.getLength());
    byte b = tgt.get();
    src.position(start);

    while (src.hasRemaining()) {
      if (b == src.get()) { // matching first byte
        src.mark(); // save position in loop
        tgt.mark(); // save position in target
        boolean found = true;
        int pos = src.position() - 1;
        while (tgt.hasRemaining()) {
          if (!src.hasRemaining()) { // src expired first
            tgt.reset();
            src.reset();
            found = false;
            break;
          }
          if (!(tgt.get() == src.get())) {
            tgt.reset();
            src.reset();
            found = false;
            break; // no match
          }
        }
        if (found) {
          return pos;
        }
      }
    }
    return -1; // not found
  }

  private GenericUDFUtils() {
    // prevent instantiation
  }
}
