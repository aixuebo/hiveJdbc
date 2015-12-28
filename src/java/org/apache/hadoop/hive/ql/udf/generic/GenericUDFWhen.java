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

import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.BooleanObjectInspector;

/**
 * GenericUDF Class for SQL construct "CASE a WHEN b THEN c [ELSE f] END".
 * 
 * NOTES: 1. a and b should have the same TypeInfo, or an exception will be
 * thrown. 2. c and f should have the same TypeInfo, or an exception will be
 * thrown.
 * 1.a和b,互相比较的类型,必须是相同的类型
 * 2.c和f返回值必须是相同的类型
 * 
 * 对case column when a then b else c end 形式进行处理
 * 注意:
 * a 类型必须是boolean类型的
 * b和c必须返回值类型相同
 */
public class GenericUDFWhen extends GenericUDF {
  private transient ObjectInspector[] argumentOIs;//参数值,针对when a then b else c进行设置参数值
  private transient GenericUDFUtils.ReturnObjectInspectorResolver returnOIResolver;

  @Override
  public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentTypeException {

    argumentOIs = arguments;
    returnOIResolver = new GenericUDFUtils.ReturnObjectInspectorResolver();

    for (int i = 0; i + 1 < arguments.length; i += 2) {
    //a对应的参数值必须是boolean类型
      if (!arguments[i].getTypeName().equals(serdeConstants.BOOLEAN_TYPE_NAME)) {
        throw new UDFArgumentTypeException(i, "\""
            + serdeConstants.BOOLEAN_TYPE_NAME + "\" is expected after WHEN, "
            + "but \"" + arguments[i].getTypeName() + "\" is found");
      }
      
      //所有的返回值必须是相同的类型
      if (!returnOIResolver.update(arguments[i + 1])) {
        throw new UDFArgumentTypeException(i + 1,
            "The expressions after THEN should have the same type: \""
            + returnOIResolver.get().getTypeName()
            + "\" is expected but \"" + arguments[i + 1].getTypeName()
            + "\" is found");
      }
    }
    
    //校验else的返回值必须与then的返回值类型相同
    if (arguments.length % 2 == 1) {
      int i = arguments.length - 2;
      if (!returnOIResolver.update(arguments[i + 1])) {
        throw new UDFArgumentTypeException(i + 1,
            "The expression after ELSE should have the same type as those after THEN: \""
            + returnOIResolver.get().getTypeName()
            + "\" is expected but \"" + arguments[i + 1].getTypeName()
            + "\" is found");
      }
    }

    return returnOIResolver.get();
  }

  @Override
  public Object evaluate(DeferredObject[] arguments) throws HiveException {
	  //每次循环都会进行对when a then b进行处理
    for (int i = 0; i + 1 < arguments.length; i += 2) {
      Object caseKey = arguments[i].get();//获取a
      if (caseKey != null
          && ((BooleanObjectInspector) argumentOIs[i]).get(caseKey)) {//必须是boolean类型的
        Object caseValue = arguments[i + 1].get();//获取value值
        return returnOIResolver.convertIfNecessary(caseValue,
            argumentOIs[i + 1]);//将返回值进行类型转换
      }
    }
    // Process else statement 处理slse部分逻辑
    if (arguments.length % 2 == 1) {
      int i = arguments.length - 2;
      Object elseValue = arguments[i + 1].get();//将else的值进行类型转换
      return returnOIResolver.convertIfNecessary(elseValue, argumentOIs[i + 1]);
    }
    return null;
  }

  @Override
  public String getDisplayString(String[] children) {
    assert (children.length >= 2);
    StringBuilder sb = new StringBuilder();
    sb.append("CASE");
    for (int i = 0; i + 1 < children.length; i += 2) {
      sb.append(" WHEN (");
      sb.append(children[i]);
      sb.append(") THEN (");
      sb.append(children[i + 1]);
      sb.append(")");
    }
    if (children.length % 2 == 1) {
      sb.append(" ELSE (");
      sb.append(children[children.length - 1]);
      sb.append(")");
    }
    sb.append(" END");
    return sb.toString();
  }

}
