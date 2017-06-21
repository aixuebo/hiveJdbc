package org.apache.hadoop.hive.serde2.typeinfo;

import org.apache.hadoop.hive.common.type.HiveVarchar;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.io.HiveVarcharWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils.PrimitiveTypeEntry;

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
 * 获取基础对象对应的参数
 */
public class ParameterizedPrimitiveTypeUtils {

	//对象为基础类型的时候,才能有参数,否则返回null
  public static BaseTypeParams getTypeParamsFromTypeInfo(TypeInfo typeInfo) {
    BaseTypeParams typeParams = null;//获取该基础类型的参数,比如字符长度等信息
    if (typeInfo instanceof PrimitiveTypeInfo) {
      PrimitiveTypeInfo ppti = (PrimitiveTypeInfo)typeInfo;
      typeParams = ppti.getTypeParams();
    }
    return typeParams;
  }

  //返回基础类型的参数对象
  public static BaseTypeParams getTypeParamsFromPrimitiveTypeEntry(PrimitiveTypeEntry typeEntry) {
    return typeEntry.typeParams;
  }

//返回基础类型的参数对象
  public static BaseTypeParams getTypeParamsFromPrimitiveObjectInspector(
      PrimitiveObjectInspector oi) {
    return oi.getTypeParams();
  }

  /**
   * Utils for varchar type
   */
  public static class HiveVarcharSerDeHelper {
    public int maxLength;
    public HiveVarcharWritable writable = new HiveVarcharWritable();

    public HiveVarcharSerDeHelper(VarcharTypeParams typeParams) {
      if (typeParams == null) {
        throw new RuntimeException("varchar type used without type params");
      }
      maxLength = typeParams.getLength();
    }
  }

    //typeParams == null,说明不需要校验verchar类型的length长度.因此返回true
    //typeParams.length >= writable.getCharacterLength() 说明规定的verchar类型的length长度 比实际的要大,因此是允许被插入的,因此返回true
    //校验是否writable对应的verchar类型的数据可以插入到输出流中,true表示可以,false表示不可以
  public static boolean doesWritableMatchTypeParams(HiveVarcharWritable writable,
      VarcharTypeParams typeParams) {
    return (typeParams == null || typeParams.length >= writable.getCharacterLength());
  }

    //校验是否java方式序列化的verchar类型的数据可以插入到输出流中,true表示可以,false表示不可以
  public static boolean doesPrimitiveMatchTypeParams(HiveVarchar value,
      VarcharTypeParams typeParams) {
    return (typeParams == null || typeParams.length == value.getCharacterLength());
  }
}
