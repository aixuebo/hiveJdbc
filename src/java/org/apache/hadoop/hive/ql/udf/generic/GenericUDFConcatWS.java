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

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils.PrimitiveGrouping;
import org.apache.hadoop.io.Text;

/**
 * Generic UDF for string function
 * <code>CONCAT_WS(sep, [string | array(string)]+)<code>.
 * This mimics the function from
 * MySQL http://dev.mysql.com/doc/refman/5.0/en/string-functions.html#
 * function_concat-ws
 *
 * @see org.apache.hadoop.hive.ql.udf.generic.GenericUDF
 * 
 * SELECT id, CONCAT_WS(',', COLLECT_SET(pic)) FROM tbl GROUP BY id
 * 可以将COLLECT_SET(pic)产生的pic数组,使用,号拆分成一个字符串
 * 
 * 
 * returns the concatenation of the strings separated by the separator.
 * 表示返回一个使用拆分符串联的字符串
 * 
 * 例如ELECT concat_ws('.', 'www', array('facebook', 'com')) FROM src LIMIT 1
 * 返回www.facebook.com
 
 注意:
 1.参数只能是字符串或者数组类型
 2.如果字符串或者数组中有null,则将会将其过滤掉
 比如
 原始数据100010	100020	100032	NULL	100051	NULL	110022
 最后返回值 concat_ws('--'), 100010--100020--100032--100051--110022
 */
@Description(name = "concat_ws",
    value = "_FUNC_(separator, [string | array(string)]+) - "
    + "returns the concatenation of the strings separated by the separator.",
    extended = "Example:\n"
    + "  > SELECT _FUNC_('.', 'www', array('facebook', 'com')) FROM src LIMIT 1;\n"
    + "  'www.facebook.com'")
public class GenericUDFConcatWS extends GenericUDF {
	
  private transient ObjectInspector[] argumentOIs;//参数类型集合

  @Override
  public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
	//参数不能小于2,因为第一个是分隔符,第二个是字符串或者数组,即等待被拆分的集合
    if (arguments.length < 2) {
      throw new UDFArgumentLengthException(
          "The function CONCAT_WS(separator,[string | array(string)]+) "
            + "needs at least two arguments.");
    }

    // check if argument is a string or an array of strings
    //校验参数是否是String或者array<String>类型的,否则抛异常
    for (int i = 0; i < arguments.length; i++) {
      switch(arguments[i].getCategory()) {
        case LIST:
          if (isStringOrVoidType(
              ((ListObjectInspector) arguments[i]).getListElementObjectInspector())) {
            break;
          }
        case PRIMITIVE:
          if (isStringOrVoidType(arguments[i])) {//允许通过
          break;
          }
        default:
        	//参数类型必须是String,或者arrat<String>类型的,否则抛异常
          throw new UDFArgumentTypeException(i, "Argument " + (i + 1)
            + " of function CONCAT_WS must be \"" + serdeConstants.STRING_TYPE_NAME
            + " or " + serdeConstants.LIST_TYPE_NAME + "<" + serdeConstants.STRING_TYPE_NAME
            + ">\", but \"" + arguments[i].getTypeName() + "\" was found.");
      }
    }

    argumentOIs = arguments;
    return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
  }

  //String和void类型的允许通过
  protected boolean isStringOrVoidType(ObjectInspector oi) {
    if (oi.getCategory() == Category.PRIMITIVE) {
      if (PrimitiveGrouping.STRING_GROUP
          == PrimitiveObjectInspectorUtils.getPrimitiveGrouping(
              ((PrimitiveObjectInspector) oi).getPrimitiveCategory())
          || ((PrimitiveObjectInspector) oi).getPrimitiveCategory() == PrimitiveCategory.VOID) {
        return true;
      }
    }
    return false;
  }

  //最终结果
  private final Text resultText = new Text();

  //追加所有的参数,追加成字符串
  @Override
  public Object evaluate(DeferredObject[] arguments) throws HiveException {
    if (arguments[0].get() == null) {
      return null;
    }
    
    //获取拆分字符串
    //将arguments[0].get()第一个参数的值,转换成argumentOIs[0]类型,即String类型的字符串
    String separator = PrimitiveObjectInspectorUtils.getString(
        arguments[0].get(), (PrimitiveObjectInspector)argumentOIs[0]);

    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (int i = 1; i < arguments.length; i++) {//循环每一个等待添加的集合
      if (arguments[i].get() != null) {
        if (first) {
          first = false;
        } else {
          sb.append(separator);
        }
        if (argumentOIs[i].getCategory().equals(Category.LIST)) {//添加一个array
          Object strArray = arguments[i].get();
          ListObjectInspector strArrayOI = (ListObjectInspector) argumentOIs[i];
          boolean strArrayFirst = true;
          for (int j = 0; j < strArrayOI.getListLength(strArray); j++) {
            if (strArrayFirst) {
              strArrayFirst = false;
            } else {
              sb.append(separator);
            }
            sb.append(strArrayOI.getListElement(strArray, j));
          }
        } else {//追加字符串值
          sb.append(PrimitiveObjectInspectorUtils.getString(
              arguments[i].get(), (PrimitiveObjectInspector)argumentOIs[i]));
        }
      }
    }

    resultText.set(sb.toString());
    return resultText;
  }

  @Override
  public String getDisplayString(String[] children) {
    assert (children.length >= 2);
    StringBuilder sb = new StringBuilder();
    sb.append("concat_ws(");
    for (int i = 0; i < children.length - 1; i++) {
      sb.append(children[i]).append(", ");
    }
    sb.append(children[children.length - 1]).append(")");
    return sb.toString();
  }
}
