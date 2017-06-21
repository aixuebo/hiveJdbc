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

package org.apache.hadoop.hive.serde2.typeinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils.PrimitiveTypeEntry;

/**
 * TypeInfoFactory can be used to create the TypeInfo object for any types.
 *
 * TypeInfo objects are all read-only so we can reuse them easily.
 * TypeInfoFactory has internal cache to make sure we don't create 2 TypeInfo
 * objects that represents the same type.
 */
public final class TypeInfoFactory {
  private static Log LOG = LogFactory.getLog(TypeInfoFactory.class);
  static ConcurrentHashMap<String, TypeInfo> cachedPrimitiveTypeInfo = new ConcurrentHashMap<String, TypeInfo>();

  private TypeInfoFactory() {
    // prevent instantiation
  }

    //参数是基本类型,比如 decimal(10, 2).或者decimal
  public static TypeInfo getPrimitiveTypeInfo(String typeName) {
	
	//根据数据类型的字符串形式,转换成具体的数据类型  
    PrimitiveTypeEntry typeEntry = PrimitiveObjectInspectorUtils
        .getTypeEntryFromTypeName(TypeInfoUtils.getBaseName(typeName));//获取基本类型的具体对象 decimal对象
    if (null == typeEntry) {
      throw new RuntimeException("Cannot getPrimitiveTypeInfo for " + typeName);//说明不支持该基本类型
    }
    
    TypeInfo result = cachedPrimitiveTypeInfo.get(typeName);
    if (result == null) {
      TypeInfoUtils.PrimitiveParts parts = TypeInfoUtils.parsePrimitiveParts(typeName);
      // Create params if there are any
      if (parts.typeParams != null && parts.typeParams.length > 0) {//说明基础类型有参数
        // The type string came with parameters.  Parse and add to TypeInfo
        try {
          BaseTypeParams typeParams = PrimitiveTypeEntry.createTypeParams(
              parts.typeName, parts.typeParams);//创建参数对象
          result = new PrimitiveTypeInfo(typeName);
          ((PrimitiveTypeInfo) result).setTypeParams(typeParams);//设置参数对象
        } catch (Exception err) {
          LOG.error(err);
          throw new RuntimeException("Error creating type parameters for " + typeName
              + ": " + err, err);
        }
      } else {//说明基础类型没有参数
        // No type params

        // Prevent creation of varchar TypeInfo with no length specification.
        // This can happen if an old-style UDF uses a varchar type either as an
        // argument or return type in an evaluate() function, or other instances
        // of using reflection-based methods for retrieving a TypeInfo.
        if (typeEntry.primitiveCategory == PrimitiveCategory.VARCHAR) {//verchar必须要有参数
          LOG.error("varchar type used with no type params");
          throw new RuntimeException("varchar type used with no type params");
        }

        result = new PrimitiveTypeInfo(parts.typeName);//因为没有参数,则该对象没有设置参数
      }

      cachedPrimitiveTypeInfo.put(typeName, result);
    }
    return result;
  }

    //定义基本类型
  public static final TypeInfo voidTypeInfo = getPrimitiveTypeInfo(serdeConstants.VOID_TYPE_NAME);
  public static final TypeInfo booleanTypeInfo = getPrimitiveTypeInfo(serdeConstants.BOOLEAN_TYPE_NAME);
  public static final TypeInfo intTypeInfo = getPrimitiveTypeInfo(serdeConstants.INT_TYPE_NAME);
  public static final TypeInfo longTypeInfo = getPrimitiveTypeInfo(serdeConstants.BIGINT_TYPE_NAME);
  public static final TypeInfo stringTypeInfo = getPrimitiveTypeInfo(serdeConstants.STRING_TYPE_NAME);
  public static final TypeInfo floatTypeInfo = getPrimitiveTypeInfo(serdeConstants.FLOAT_TYPE_NAME);
  public static final TypeInfo doubleTypeInfo = getPrimitiveTypeInfo(serdeConstants.DOUBLE_TYPE_NAME);
  public static final TypeInfo byteTypeInfo = getPrimitiveTypeInfo(serdeConstants.TINYINT_TYPE_NAME);
  public static final TypeInfo shortTypeInfo = getPrimitiveTypeInfo(serdeConstants.SMALLINT_TYPE_NAME);
  public static final TypeInfo dateTypeInfo = getPrimitiveTypeInfo(serdeConstants.DATE_TYPE_NAME);
  public static final TypeInfo timestampTypeInfo = getPrimitiveTypeInfo(serdeConstants.TIMESTAMP_TYPE_NAME);
  public static final TypeInfo binaryTypeInfo = getPrimitiveTypeInfo(serdeConstants.BINARY_TYPE_NAME);
  public static final TypeInfo decimalTypeInfo = getPrimitiveTypeInfo(serdeConstants.DECIMAL_TYPE_NAME);
  // Disallow usage of varchar without length specifier.
  //public static final TypeInfo varcharTypeInfo = getPrimitiveTypeInfo(serdeConstants.VARCHAR_TYPE_NAME);

  public static final TypeInfo unknownTypeInfo = getPrimitiveTypeInfo("unknown");

  //通过hadoop原始类型或者包装类型,获取hive对应的typeName
  public static TypeInfo getPrimitiveTypeInfoFromPrimitiveWritable(
      Class<?> clazz) {
    String typeName = PrimitiveObjectInspectorUtils
        .getTypeNameFromPrimitiveWritable(clazz);
    if (typeName == null) {
      throw new RuntimeException("Internal error: Cannot get typeName for "
          + clazz);
    }
    return getPrimitiveTypeInfo(typeName);
  }

  //通过java原始类型或者包装类型,获取hive对应的typeName
  public static TypeInfo getPrimitiveTypeInfoFromJavaPrimitive(Class<?> clazz) {
    return getPrimitiveTypeInfo(PrimitiveObjectInspectorUtils
        .getTypeNameFromPrimitiveJava(clazz));
  }

    //key以及对应的类型映射---作为缓存
  static ConcurrentHashMap<ArrayList<List<?>>, TypeInfo> cachedStructTypeInfo =
    new ConcurrentHashMap<ArrayList<List<?>>, TypeInfo>();

    //struct持有两个集合,一个是属性集合,一个是属性对应的类型集合
  public static TypeInfo getStructTypeInfo(List<String> names,
      List<TypeInfo> typeInfos) {
    ArrayList<List<?>> signature = new ArrayList<List<?>>(2);
    signature.add(names);
    signature.add(typeInfos);
    TypeInfo result = cachedStructTypeInfo.get(signature);
    if (result == null) {
      result = new StructTypeInfo(names, typeInfos);
      cachedStructTypeInfo.put(signature, result);
    }
    return result;
  }

  static ConcurrentHashMap<List<?>, TypeInfo> cachedUnionTypeInfo =
    new ConcurrentHashMap<List<?>, TypeInfo>();

    //union持有一个类型集合
  public static TypeInfo getUnionTypeInfo(List<TypeInfo> typeInfos) {
    TypeInfo result = cachedUnionTypeInfo.get(typeInfos);
    if (result == null) {
      result = new UnionTypeInfo(typeInfos);
      cachedUnionTypeInfo.put(typeInfos, result);
    }
    return result;
  }

  static ConcurrentHashMap<TypeInfo, TypeInfo> cachedListTypeInfo = new ConcurrentHashMap<TypeInfo, TypeInfo>();

    //array或者list持有一个对象
  public static TypeInfo getListTypeInfo(TypeInfo elementTypeInfo) {
    TypeInfo result = cachedListTypeInfo.get(elementTypeInfo);
    if (result == null) {
      result = new ListTypeInfo(elementTypeInfo);
      cachedListTypeInfo.put(elementTypeInfo, result);
    }
    return result;
  }

  static ConcurrentHashMap<ArrayList<TypeInfo>, TypeInfo> cachedMapTypeInfo =
    new ConcurrentHashMap<ArrayList<TypeInfo>, TypeInfo>();

    //map持有两个对象,一个是key对象,一个是value对象,即2种类型
  public static TypeInfo getMapTypeInfo(TypeInfo keyTypeInfo,
      TypeInfo valueTypeInfo) {
    ArrayList<TypeInfo> signature = new ArrayList<TypeInfo>(2);
    signature.add(keyTypeInfo);
    signature.add(valueTypeInfo);
    TypeInfo result = cachedMapTypeInfo.get(signature);
    if (result == null) {
      result = new MapTypeInfo(keyTypeInfo, valueTypeInfo);
      cachedMapTypeInfo.put(signature, result);
    }
    return result;
  };

}
