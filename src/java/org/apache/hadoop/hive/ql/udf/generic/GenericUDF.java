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

import java.io.Closeable;
import java.io.IOException;

import org.apache.hadoop.hive.ql.exec.MapredContext;
import org.apache.hadoop.hive.ql.exec.FunctionRegistry;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.serde2.objectinspector.ConstantObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;

/**
 * A Generic User-defined function (GenericUDF) for the use with Hive.
 *
 * New GenericUDF classes need to inherit from this GenericUDF class.
 *
 * The GenericUDF are superior to normal UDFs in the following ways: 1. It can
 * accept arguments of complex types, and return complex types. 2. It can accept
 * variable length of arguments. 3. It can accept an infinite number of function
 * signature - for example, it's easy to write a GenericUDF that accepts
 * array<int>, array<array<int>> and so on (arbitrary levels of nesting). 4. It
 * can do short-circuit evaluations using DeferedObject.
 */
@UDFType(deterministic = true)
public abstract class GenericUDF implements Closeable {

  /**
   * A Defered Object allows us to do lazy-evaluation and short-circuiting.
   * GenericUDF use DeferedObject to pass arguments.
   * 延期对象,允许我们去做懒加载
   */
  public static interface DeferredObject {
    void prepare(int version) throws HiveException;
    Object get() throws HiveException;
  };

  /**
   * A basic dummy implementation of DeferredObject which just stores a Java
   * Object reference.
   */
  public static class DeferredJavaObject implements DeferredObject {
    private Object value;

    public DeferredJavaObject(Object value) {
      this.value = value;
    }

    @Override
    public void prepare(int version) throws HiveException {
    }

    @Override
    public Object get() throws HiveException {
      return value;
    }
  }

  /**
   * The constructor.
   */
  public GenericUDF() {
  }

  /**
   * Initialize this GenericUDF. This will be called once and only once per
   * GenericUDF instance.
   * 初始化函数
   * @param arguments
   *          The ObjectInspector for the arguments 函数的参数类型集合
   * @throws UDFArgumentException
   *           Thrown when arguments have wrong types, wrong length, etc. 参数校验失败,产生的异常
   * @return The ObjectInspector for the return value 函数的返回值类型
   */
  public abstract ObjectInspector initialize(ObjectInspector[] arguments)
      throws UDFArgumentException;

  /**
   * Additionally setup GenericUDF with MapredContext before initializing.
   * This is only called in runtime of MapRedTask.
   *
   * @param context context
   */
  public void configure(MapredContext context) {
  }

  /**
   * Initialize this GenericUDF.  Additionally, if the arguments are constant
   * and the function is eligible to be folded, then the constant value
   * returned by this UDF will be computed and stored in the
   * ConstantObjectInspector returned.  Otherwise, the function behaves exactly
   * like initialize().
   * 初始化,并且附加了一些操作,即会将结果缓存起来,因为结果也是常量结果
   * 1.如果参数都是常量对象ConstantObjectInspector
   * 2.并且该函数是一个合格的,可以被folded的函数
   * 3.返回值也是一个常量对象ConstantObjectInspector
   * 因此这样的自定义函数是会被预先编译,并且存储在返回值常量里面,否则该函数仅仅执行initialize初始化方法,即与initialize方法被调用没有任何区别
   */
  public ObjectInspector initializeAndFoldConstants(ObjectInspector[] arguments)
      throws UDFArgumentException {

    ObjectInspector oi = initialize(arguments);//返回值

    // If the UDF depends on any external resources, we can't fold because the
    // resources may not be available at compile time.
    //如果该函数依赖其他的额外资源,我们不会fold,因为这些资源在编译器还不可用
    if (getRequiredFiles() != null ||
        getRequiredJars() != null) {
      return oi;
    }

    boolean allConstant = true;//默认所有参数都是常量,如果该值是false,说明其中有任意一个参数不是常量ConstantObjectInspector对象
    for (int ii = 0; ii < arguments.length; ++ii) {
      if (!ObjectInspectorUtils.isConstantObjectInspector(arguments[ii])) {//该对象是否是常量对象
        allConstant = false;
        break;
      }
    }

    /**
     * 1.如果全部参数都是常量对象ConstantObjectInspector,
     * 2.返回值并不是常量对象ConstantObjectInspector
     * 3.
     */
    if (allConstant &&
        !ObjectInspectorUtils.isConstantObjectInspector(oi) &&
        FunctionRegistry.isDeterministic(this) &&
        !FunctionRegistry.isStateful(this) &&
        ObjectInspectorUtils.supportsConstantObjectInspector(oi)) {
    	
    	//将常量参数组装成对象数组
      DeferredObject[] argumentValues =
        new DeferredJavaObject[arguments.length];
      for (int ii = 0; ii < arguments.length; ++ii) {
        argumentValues[ii] = new DeferredJavaObject(
            ((ConstantObjectInspector)arguments[ii]).getWritableConstantValue());
      }
      try {
    	//执行该函数,并且返回常量对象
        Object constantValue = evaluate(argumentValues);
        oi = ObjectInspectorUtils.getConstantObjectInspector(oi, constantValue);
      } catch (HiveException e) {
        throw new UDFArgumentException(e);
      }
    }
    return oi;
  }

  /**
   * The following two functions can be overridden to automatically include
   * additional resources required by this UDF.  The return types should be
   * arrays of paths.
   * 该自定义函数可能需要外部jar资源
   */
  public String[] getRequiredJars() {
    return null;
  }

  //该自定义函数可能需要外部文件资源
  public String[] getRequiredFiles() {
    return null;
  }

  /**
   * Evaluate the GenericUDF with the arguments.
   *
   * @param arguments
   *          The arguments as DeferedObject, use DeferedObject.get() to get the
   *          actual argument Object. The Objects can be inspected by the
   *          ObjectInspectors passed in the initialize call.
   * @return The
   * 具体的参数值传递进来,该参数与init方法对应的参数类型要匹配,返回值要与init对应的返回值匹配
   */
  public abstract Object evaluate(DeferredObject[] arguments)
      throws HiveException;

  /**
   * Get the String to be displayed in explain.
   */
  public abstract String getDisplayString(String[] children);

  /**
   * Close GenericUDF.
   * This is only called in runtime of MapRedTask.
   */
  public void close() throws IOException {
  }
}
