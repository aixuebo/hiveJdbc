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

import org.apache.hadoop.hive.common.type.HiveVarchar;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.BinaryObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorConverter.StringConverter;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.typeinfo.VarcharTypeParams;
import org.apache.hadoop.io.BytesWritable;

/**
 * GenericUDFConcat.
 * 将参数集合连接成串
 * 
 * 例如:
 * SELECT concat('abc', 'def') FROM src LIMIT 1,返回abcdef
 * 
 * 注意:
 * 1.如果有任意一个字符是null,则返回null
 * 2.仅仅支持java的基础类型作为参数
 */
@Description(name = "concat",
value = "_FUNC_(str1, str2, ... strN) - returns the concatenation of str1, str2, ... strN or "+
        "_FUNC_(bin1, bin2, ... binN) - returns the concatenation of bytes in binary data " +
        " bin1, bin2, ... binN",
extended = "Returns NULL if any argument is NULL.\n"
+ "Example:\n"
+ "  > SELECT _FUNC_('abc', 'def') FROM src LIMIT 1;\n"
+ "  'abcdef'")
public class GenericUDFConcat extends GenericUDF {
  private transient ObjectInspector[] argumentOIs;
  private transient StringConverter[] stringConverters;
  private transient PrimitiveCategory returnType = PrimitiveCategory.STRING;//返回类型,默认是String类型
  private transient BytesWritable[] bw;
  private transient GenericUDFUtils.StringHelper returnHelper;

  @Override
  public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

    // Loop through all the inputs to determine the appropriate return type/length.
    // Either all arguments are binary, or all columns are non-binary.所有的参数类型必须一致,要么是二进制，要么是非二进制
    // Return type:
    //  All VARCHAR inputs: return VARCHAR 所有的输入是verchar,则返回verchar
    //  All BINARY inputs: return BINARY 所有的输入是BINARY,则返回BINARY
    //  Otherwise return STRING  其他的输入,则返回字符串
    argumentOIs = arguments;

    PrimitiveCategory currentCategory;
    PrimitiveObjectInspector poi;
    boolean fixedLengthReturnValue = true;//返回值是否固定
    int returnLength = 0;  // Only for char/varchar return types 仅仅使用在char/varchar的返回值中
    for (int idx = 0; idx < arguments.length; ++idx) {
      
      //校验仅仅支持java的基础类型作为参数
      if (arguments[idx].getCategory() != Category.PRIMITIVE) {
        throw new UDFArgumentException("CONCAT only takes primitive arguments");
      }
      poi = (PrimitiveObjectInspector)arguments[idx];
      currentCategory = poi.getPrimitiveCategory();
      if (idx == 0) {//第一个参数类型就是返回的类型
        returnType = currentCategory;
      }
      
      switch (currentCategory) {
        case BINARY:
          fixedLengthReturnValue = false;
          if (returnType != currentCategory) {
            throw new UDFArgumentException(
                "CONCAT cannot take a mix of binary and non-binary arguments");
          }
          break;
        case VARCHAR:
          if (returnType == PrimitiveCategory.BINARY) {
            throw new UDFArgumentException(
                "CONCAT cannot take a mix of binary and non-binary arguments");
          }
          break;
        default:
          if (returnType == PrimitiveCategory.BINARY) {
            throw new UDFArgumentException(
                "CONCAT cannot take a mix of binary and non-binary arguments");
          }
          returnType = PrimitiveCategory.STRING;
          fixedLengthReturnValue = false;
          break;
      }

      // If all arguments are of known length then we can keep track of the max
      // length of the return type. However if the return length exceeds the
      // max length for the char/varchar, then the return type reverts to string.
      if (fixedLengthReturnValue) {
        returnLength += GenericUDFUtils.StringHelper.getFixedStringSizeForType(poi);
        if (returnType == PrimitiveCategory.VARCHAR
            && returnLength > HiveVarchar.MAX_VARCHAR_LENGTH) {
          returnType = PrimitiveCategory.STRING;
          fixedLengthReturnValue = false;
        }
      }
    }

    if (returnType == PrimitiveCategory.BINARY) {
      bw = new BytesWritable[arguments.length];
      return PrimitiveObjectInspectorFactory.writableBinaryObjectInspector;
    } else {
      // treat all inputs as string, the return value will be converted to the appropriate type.
      createStringConverters();
      returnHelper = new GenericUDFUtils.StringHelper(returnType);
      switch (returnType) {
        case STRING:
          return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
        case VARCHAR:
          VarcharTypeParams varcharParams = new VarcharTypeParams();
          varcharParams.setLength(returnLength);
          return PrimitiveObjectInspectorFactory.getPrimitiveWritableObjectInspector(
              PrimitiveObjectInspectorUtils.getTypeEntryFromTypeSpecs(returnType, varcharParams));
        default:
          throw new UDFArgumentException("Unexpected CONCAT return type of " + returnType);
      }
    }
  }

  private void createStringConverters() {
    stringConverters = new StringConverter[argumentOIs.length];
    for (int idx = 0; idx < argumentOIs.length; ++idx) {
      stringConverters[idx] = new StringConverter((PrimitiveObjectInspector) argumentOIs[idx]);
    }
  }

  @Override
  public Object evaluate(DeferredObject[] arguments) throws HiveException {
    if (returnType == PrimitiveCategory.BINARY) {
      return binaryEvaluate(arguments);
    } else {
      return returnHelper.setReturnValue(stringEvaluate(arguments));
    }
  }

  /**
   * 将二进制数组数据转入到一个BytesWritable中
   */
  public Object binaryEvaluate(DeferredObject[] arguments) throws HiveException {
    int len = 0;
    for (int idx = 0; idx < arguments.length; ++idx) {
      bw[idx] = ((BinaryObjectInspector)argumentOIs[idx])
          .getPrimitiveWritableObject(arguments[idx].get());
      if (bw[idx] == null){
        return null;
      }
      len += bw[idx].getLength();
    }

    byte[] out = new byte[len];
    int curLen = 0;
    // Need to iterate twice since BytesWritable doesn't support append.
    for (BytesWritable bytes : bw){
      System.arraycopy(bytes.getBytes(), 0, out, curLen, bytes.getLength());
      curLen += bytes.getLength();
    }
    return new BytesWritable(out);
  }

  /**
   * 将String参数集合转换成一个String
   * 
   * 如果有一个参数是null,则返回值就是null
   */
  public String stringEvaluate(DeferredObject[] arguments) throws HiveException {
    StringBuilder sb = new StringBuilder();
    for (int idx = 0; idx < arguments.length; ++idx) {
      String val = null;
      if (arguments[idx] != null) {
        val = (String) stringConverters[idx].convert(arguments[idx].get());
      }
      if (val == null) {
        return null;
      }
      sb.append(val);
    }
    return sb.toString();
  }

  @Override
  public String getDisplayString(String[] children) {
    StringBuilder sb = new StringBuilder();
    sb.append("concat(");
    if (children.length > 0) {
      sb.append(children[0]);
      for (int i = 1; i < children.length; i++) {
        sb.append(", ");
        sb.append(children[i]);
      }
    }
    sb.append(")");
    return sb.toString();
  }

}
