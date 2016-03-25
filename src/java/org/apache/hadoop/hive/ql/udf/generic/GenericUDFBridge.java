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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.hadoop.hive.ql.exec.FunctionRegistry;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils.ConversionHelper;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory.ObjectInspectorOptions;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * GenericUDFBridge encapsulates UDF to provide the same interface as
 * GenericUDF.
 *
 * Note that GenericUDFBridge implements Serializable because the name of the
 * UDF class needs to be serialized with the plan.
 * 定义一个自定义函数的名字、class全路径以及class对象,支持序列化服务,用于plan计划之间的传输
 *
 */
public class GenericUDFBridge extends GenericUDF implements Serializable {
  /**
   * The name of the UDF.
   */
  String udfName;

  /**
   * Whether the UDF is an operator or not. This controls how the display string
   * is generated.
   * 如果该值为true,则函数是以下方式写入的
   * 1.(param2 udfName param1)
   * 2.(udfName param1)
   * 如果该值是false,则函数是正常方式写入的,即udfName(param1,param2,param3)
   */
  boolean isOperator;

  /**
   * The underlying UDF class.
   */
  Class<? extends UDF> udfClass;

  /**
   * The underlying UDF class Name.
   */
  String udfClassName;

  /**
   * Greate a new GenericUDFBridge object.
   *
   * @param udfName
   *          The name of the corresponding udf.
   * @param isOperator
   * @param udfClass
   */
  public GenericUDFBridge(String udfName, boolean isOperator,
      Class<? extends UDF> udfClass) {
    this.udfName = udfName;
    this.isOperator = isOperator;
    this.udfClass = udfClass;
    this.udfClassName = udfClass != null ? udfClass.getName() : null;
  }

  // For Java serialization only
  public GenericUDFBridge() {
  }

  public void setUdfName(String udfName) {
    this.udfName = udfName;
  }

  public String getUdfName() {
    return udfName;
  }

  public String getUdfClassName() {
    return udfClassName;
  }

  public void setUdfClassName(String udfClassName) {
    this.udfClassName = udfClassName;
  }

  public boolean isOperator() {
    return isOperator;
  }

  public void setOperator(boolean isOperator) {
    this.isOperator = isOperator;
  }

  public void setUdfClass(Class<? extends UDF> udfClass) {
    this.udfClass = udfClass;
  }

  public Class<? extends UDF> getUdfClass() {
    return udfClass;
  }

  /**
   * The underlying method of the UDF class.
   */
  transient Method udfMethod;

  /**
   * Helper to convert the parameters before passing to udfMethod.
   */
  transient ConversionHelper conversionHelper;
  /**
   * The actual udf object.
   * 通过反射获取自定义函数对象
   */
  transient UDF udf;
  /**
   * The non-deferred real arguments for method invocation.
   * 用来存储参数
   */
  transient Object[] realArguments;

  @Override
  public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

    if (udfClass == null) {
      throw new UDFArgumentException(
          "The UDF implementation class '" + udfClassName
              + "' is not present in the class path");
    }
    udf = (UDF) ReflectionUtils.newInstance(udfClass, null);

    // Resolve for the method based on argument types获取参数集合
    ArrayList<TypeInfo> argumentTypeInfos = new ArrayList<TypeInfo>(
        arguments.length);
    for (ObjectInspector argument : arguments) {
      argumentTypeInfos.add(TypeInfoUtils
          .getTypeInfoFromObjectInspector(argument));
    }
    
    //获取要执行自定义函数类的哪个方法
    udfMethod = udf.getResolver().getEvalMethod(argumentTypeInfos);
    udfMethod.setAccessible(true);

    // Create parameter converters
    conversionHelper = new ConversionHelper(udfMethod, arguments);

    // Create the non-deferred realArgument
    realArguments = new Object[arguments.length];

    // Get the return ObjectInspector.获取返回类型
    ObjectInspector returnOI = ObjectInspectorFactory
        .getReflectionObjectInspector(udfMethod.getGenericReturnType(),
        ObjectInspectorOptions.JAVA);

    return returnOI;
  }

  //执行该函数
  @Override
  public Object evaluate(DeferredObject[] arguments) throws HiveException {
    assert (arguments.length == realArguments.length);

    // Calculate all the arguments
    for (int i = 0; i < realArguments.length; i++) {
      realArguments[i] = arguments[i].get();
    }

    // Call the function
    Object result = FunctionRegistry.invoke(udfMethod, udf, conversionHelper
        .convertIfNecessary(realArguments));

    return result;
  }

  /**
   * 参数是自定义函数需要的参数值
   * 相当于函数的toString方法
   */
  @Override
  public String getDisplayString(String[] children) {
    if (isOperator) {
    	//打印(udfName param1)
      if (children.length == 1) {
        // Prefix operator
        return "(" + udfName + " " + children[0] + ")";
      } else {//打印(param2 udfName param1)
        // Infix operator
        assert children.length == 2;
        return "(" + children[0] + " " + udfName + " " + children[1] + ")";
      }
    } else {
    	//打印 udfName(param1,param2,param3)
      StringBuilder sb = new StringBuilder();
      sb.append(udfName);
      sb.append("(");
      for (int i = 0; i < children.length; i++) {
        sb.append(children[i]);
        if (i + 1 < children.length) {
          sb.append(", ");
        }
      }
      sb.append(")");
      return sb.toString();
    }
  }

  @Override
  public String[] getRequiredJars() {
    return udf.getRequiredJars();
  }

  @Override
  public String[] getRequiredFiles() {
    return udf.getRequiredFiles();
  }

}
