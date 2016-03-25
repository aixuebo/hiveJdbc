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

package org.apache.hadoop.hive.ql.exec;

import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFBridge;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFBridge;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.ql.udf.ptf.TableFunctionResolver;
import org.apache.hadoop.hive.ql.udf.ptf.WindowingTableFunction;
import org.apache.hadoop.hive.ql.udf.ptf.NoopWithMap.NoopWithMapResolver;

/**
 * FunctionInfo.
 * 定义一个函数对象
 * UDF是自定义函数
 * UDTF
 * UDAF是聚合函数
 * TableFunctionResolver是定义tableFunction
 * 定义一个函数信息
 */
public class FunctionInfo {

  private final boolean isNative;//true表示hive自身提高的函数

  private final boolean isInternalTableFunction;//true表示内部table函数,不能被外界使用,例如WindowingTableFunction

  private final String displayName;//给用户看的名字,一般是functionName的小写形式

  private GenericUDF genericUDF;//udf函数

  private GenericUDTF genericUDTF;//udtf函数,例如  registerGenericUDTF("parse_url_tuple", GenericUDTFParseUrlTuple.class);

  private GenericUDAFResolver genericUDAFResolver;//定义聚合函数UDAF,例如registerGenericUDAF("max", new GenericUDAFMax());

//registerTableFunction(NOOP_MAP_TABLE_FUNCTION, NoopWithMapResolver.class);定义table函数
  private Class<? extends TableFunctionResolver>  tableFunctionResolver;

  public FunctionInfo(boolean isNative, String displayName,
      GenericUDF genericUDF) {
    this.isNative = isNative;
    this.displayName = displayName;
    this.genericUDF = genericUDF;
    this.isInternalTableFunction = false;
  }

  public FunctionInfo(boolean isNative, String displayName,
      GenericUDAFResolver genericUDAFResolver) {
    this.isNative = isNative;
    this.displayName = displayName;
    this.genericUDAFResolver = genericUDAFResolver;
    this.isInternalTableFunction = false;
  }

  public FunctionInfo(boolean isNative, String displayName,
      GenericUDTF genericUDTF) {
    this.isNative = isNative;
    this.displayName = displayName;
    this.genericUDTF = genericUDTF;
    this.isInternalTableFunction = false;
  }

  /**
   * registerTableFunction(NOOP_MAP_TABLE_FUNCTION, NoopWithMapResolver.class);
   * 定义table函数
   * @param displayName
   * @param tFnCls
   */
  public FunctionInfo(String displayName, Class<? extends TableFunctionResolver> tFnCls)
  {
    this.displayName = displayName;
    this.tableFunctionResolver = tFnCls;
    PartitionTableFunctionDescription def = tableFunctionResolver.getAnnotation(PartitionTableFunctionDescription.class);
    this.isNative = (def == null) ? false : def.isInternal();
    this.isInternalTableFunction = isNative;
  }

  /**
   * Get a new GenericUDF object for the function.
   */
  public GenericUDF getGenericUDF() {
    // GenericUDF is stateful - we have to make a copy here
    if (genericUDF == null) {
      return null;
    }
    return FunctionRegistry.cloneGenericUDF(genericUDF);
  }

  /**
   * Get a new GenericUDTF object for the function.
   */
  public GenericUDTF getGenericUDTF() {
    // GenericUDTF is stateful too, copy
    if (genericUDTF == null) {
      return null;
    }
    return FunctionRegistry.cloneGenericUDTF(genericUDTF);
  }

  /**
   * Get the GenericUDAFResolver object for the function.
   */
  public GenericUDAFResolver getGenericUDAFResolver() {
    return genericUDAFResolver;
  }



  /**
   * Get the Class of the UDF.
   */
  public Class<?> getFunctionClass() {
    if (isGenericUDF()) {
      if (genericUDF instanceof GenericUDFBridge) {
        return ((GenericUDFBridge) genericUDF).getUdfClass();
      } else {
        return genericUDF.getClass();
      }
    } else if (isGenericUDAF()) {
      if (genericUDAFResolver instanceof GenericUDAFBridge) {
        return ((GenericUDAFBridge) genericUDAFResolver).getUDAFClass();
      } else {
        return genericUDAFResolver.getClass();
      }
    } else if (isGenericUDTF()) {
      return genericUDTF.getClass();
    }
    if(isTableFunction()) {
      return this.tableFunctionResolver;
    }
    return null;
  }

  /**
   * Get the display name for this function. This should be transfered into
   * exprNodeGenericUDFDesc, and will be used as the first parameter to
   * GenericUDF.getDisplayName() call, instead of hard-coding the function name.
   * This will solve the problem of displaying only one name when a udf is
   * registered under 2 names.
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Native functions cannot be unregistered.
   */
  public boolean isNative() {
    return isNative;
  }

  /**
   * Internal table functions cannot be used in the language.
   * 内部方法,不能被外界使用,例如WindowingTableFunction
   * {@link WindowingTableFunction}
   */
  public boolean isInternalTableFunction() {
    return isInternalTableFunction;
  }

  /**
   * @return TRUE if the function is a GenericUDF
   * 是UDF函数
   */
  public boolean isGenericUDF() {
    return null != genericUDF;
  }

  /**
   * @return TRUE if the function is a GenericUDAF
   * 是否是聚合函数UDAF
   */
  public boolean isGenericUDAF() {
    return null != genericUDAFResolver;
  }

  /**
   * @return TRUE if the function is a GenericUDTF
   */
  public boolean isGenericUDTF() {
    return null != genericUDTF;
  }

  /**
   * @return TRUE if the function is a Table Function
   */
  public boolean isTableFunction() {
    return null != tableFunctionResolver;
  }
}
