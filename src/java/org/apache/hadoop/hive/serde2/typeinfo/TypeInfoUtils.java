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

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.common.type.HiveVarchar;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.MapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.UnionObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils.PrimitiveTypeEntry;

/**
 * TypeInfoUtils.
 * 关于类型的工具类
 */
public final class TypeInfoUtils {

  private TypeInfoUtils() {
    // prevent instantiation
  }

  /**
   * Return the extended TypeInfo from a Java type. By extended TypeInfo, we
   * allow unknownType for java.lang.Object.
   * 从一个java的具体类型,返回对应的TypeInfo
   * 比如list<Map<String,String> 表示最终是一个List对象,里面套着Map对象,map是由String和String组成的schema
   * @param t
   *          The Java type.具体的java类型
   * @param m
   *          The method, only used for generating error messages.
   */
  private static TypeInfo getExtendedTypeInfoFromJavaType(Type t, Method m) {

    if (t == Object.class) {//如果一个对象是object类型的,其实是没办法知道他具体类型的,因此返回unknownTypeInfo类型
      return TypeInfoFactory.unknownTypeInfo;
    }

    if (t instanceof ParameterizedType) {//说明是泛型类型
      ParameterizedType pt = (ParameterizedType) t;
      // List?
      if (List.class == (Class<?>) pt.getRawType()
          || ArrayList.class == (Class<?>) pt.getRawType()) {//说明原始类型是List
        return TypeInfoFactory.getListTypeInfo(getExtendedTypeInfoFromJavaType(
            pt.getActualTypeArguments()[0], m));//递归继续从List的泛型对象中获取具体Type
      }
      // Map?
      if (Map.class == (Class<?>) pt.getRawType()
          || HashMap.class == (Class<?>) pt.getRawType()) {//说明是Map类型
        return TypeInfoFactory.getMapTypeInfo(getExtendedTypeInfoFromJavaType(
            pt.getActualTypeArguments()[0], m),
            getExtendedTypeInfoFromJavaType(pt.getActualTypeArguments()[1], m));//分别将第0个和第1个位置的泛型对应的java对象,递归处理成具体的TypeInfo
      }
      // Otherwise convert t to RawType so we will fall into the following if
      // block.
      t = pt.getRawType();
    }

    // Must be a class.
    if (!(t instanceof Class)) {
      throw new RuntimeException("Hive does not understand type " + t
          + " from " + m);
    }
    Class<?> c = (Class<?>) t;

    // Java Primitive Type?
    if (PrimitiveObjectInspectorUtils.isPrimitiveJavaType(c)) {//说明该class是原始对象
      return TypeInfoUtils
          .getTypeInfoFromObjectInspector(PrimitiveObjectInspectorFactory
          .getPrimitiveJavaObjectInspector(PrimitiveObjectInspectorUtils
          .getTypeEntryFromPrimitiveJavaType(c).primitiveCategory));
    }

    // Java Primitive Class?
    if (PrimitiveObjectInspectorUtils.isPrimitiveJavaClass(c)) {
      return TypeInfoUtils
          .getTypeInfoFromObjectInspector(PrimitiveObjectInspectorFactory
          .getPrimitiveJavaObjectInspector(PrimitiveObjectInspectorUtils
          .getTypeEntryFromPrimitiveJavaClass(c).primitiveCategory));
    }

    // Primitive Writable class?
    if (PrimitiveObjectInspectorUtils.isPrimitiveWritableClass(c)) {
      return TypeInfoUtils
          .getTypeInfoFromObjectInspector(PrimitiveObjectInspectorFactory
          .getPrimitiveWritableObjectInspector(PrimitiveObjectInspectorUtils
          .getTypeEntryFromPrimitiveWritableClass(c).primitiveCategory));
    }

    // Must be a struct
    Field[] fields = ObjectInspectorUtils.getDeclaredNonStaticFields(c);
    ArrayList<String> fieldNames = new ArrayList<String>(fields.length);
    ArrayList<TypeInfo> fieldTypeInfos = new ArrayList<TypeInfo>(fields.length);
    for (Field field : fields) {
      fieldNames.add(field.getName());
      fieldTypeInfos.add(getExtendedTypeInfoFromJavaType(
          field.getGenericType(), m));
    }
    return TypeInfoFactory.getStructTypeInfo(fieldNames, fieldTypeInfos);
  }

  /**
   * Returns the array element type, if the Type is an array (Object[]), or
   * GenericArrayType (Map<String,String>[]). Otherwise return null.
   */
  public static Type getArrayElementType(Type t) {
    if (t instanceof Class && ((Class<?>) t).isArray()) {//说明该参数是一个数组
      Class<?> arrayClass = (Class<?>) t;
      return arrayClass.getComponentType();
    } else if (t instanceof GenericArrayType) {
      GenericArrayType arrayType = (GenericArrayType) t;
      return arrayType.getGenericComponentType();
    }
    return null;
  }

  /**
   * Get the parameter TypeInfo for a method.
   *
   * @param size
   *          In case the last parameter of Method is an array, we will try to
   *          return a List<TypeInfo> with the specified size by repeating the
   *          element of the array at the end. In case the size is smaller than
   *          the minimum possible number of arguments for the method, null will
   *          be returned.
   *  返回该方法的参数集合,参数个数size,方法对象Method
   */
  public static List<TypeInfo> getParameterTypeInfos(Method m, int size) {
    Type[] methodParameterTypes = m.getGenericParameterTypes();//该方法的全部参数集合

    // Whether the method takes variable-length arguments
    // Whether the method takes an array like Object[],
    // or String[] etc in the last argument.
    Type lastParaElementType = TypeInfoUtils
        .getArrayElementType(methodParameterTypes.length == 0 ? null
        : methodParameterTypes[methodParameterTypes.length - 1]);//获取最后一个参数,判断他是否是可变化参数,不是可变化的参数都返回null
    boolean isVariableLengthArgument = (lastParaElementType != null);//最后一个参数是否是可变化参数

    List<TypeInfo> typeInfos = null;//返回该方法的参数集合
    if (!isVariableLengthArgument) {//不是可变化参数
      // Normal case, no variable-length arguments
      if (size != methodParameterTypes.length) {//说明该方法的参数与给定的size参数是不同的,则返回null
        return null;
      }
      typeInfos = new ArrayList<TypeInfo>(methodParameterTypes.length);
      for (Type methodParameterType : methodParameterTypes) {
        typeInfos.add(getExtendedTypeInfoFromJavaType(methodParameterType, m));
      }
    } else {//最后一个参数是可变化参数
      // Variable-length arguments
      if (size < methodParameterTypes.length - 1) {
        return null;
      }
      typeInfos = new ArrayList<TypeInfo>(size);
      for (int i = 0; i < methodParameterTypes.length - 1; i++) {
        typeInfos.add(getExtendedTypeInfoFromJavaType(methodParameterTypes[i],
            m));
      }
      for (int i = methodParameterTypes.length - 1; i < size; i++) {
        typeInfos.add(getExtendedTypeInfoFromJavaType(lastParaElementType, m));
      }
    }
    return typeInfos;
  }

  /**
   * @param typeName 可以是char(10) or decimal(10, 2).
   * @return
   */
  public static boolean hasParameters(String typeName) {
    int idx = typeName.indexOf('(');
    if (idx == -1) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * @param typeName 可以是char(10) or decimal(10, 2).
   * @return 返回char或者decimal
   */
  public static String getBaseName(String typeName) {
    int idx = typeName.indexOf('(');
    if (idx == -1) {
      return typeName;
    } else {
      return typeName.substring(0, idx);
    }
  }

  /**
   * returns true if both TypeInfos are of primitive type, and the primitive category matches.
   * @param ti1
   * @param ti2
   * @return true表示两个参数都是原始类型的参数.并且原始类型都一样
   */
  public static boolean doPrimitiveCategoriesMatch(TypeInfo ti1, TypeInfo ti2) {
    if (ti1.getCategory() == Category.PRIMITIVE && ti2.getCategory() == Category.PRIMITIVE) {
      if (((PrimitiveTypeInfo)ti1).getPrimitiveCategory()
          == ((PrimitiveTypeInfo)ti2).getPrimitiveCategory()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Parse a recursive TypeInfo list String. For example, the following inputs
   * are valid inputs:
   * "int,string,map<string,int>,list<map<int,list<string>>>,list<struct<a:int,b:string>>"
   * The separators between TypeInfos can be ",", ":", or ";".
   *
   * In order to use this class: TypeInfoParser parser = new
   * TypeInfoParser("int,string"); ArrayList<TypeInfo> typeInfos =
   * parser.parseTypeInfos();
   *
   schema的定义是struct<name:TypeInfo,name:TypeInfo>
   schema定义map<TypeInfo,TypeInfo>
   schema定义array<TypeInfo>
   schema定义uniontype<TypeInfo,TypeInfo>
   原始类型定义TypeInfo--比如decimal(20,1)
   */
  private static class TypeInfoParser {

    private static class Token {
      public int position;//开始位置偏移量
      public String text;//该token的真实值
      public boolean isType;//true表示该token代表一个类型,false则不代表类型,比如<就表示分隔符token,因此他就是false

      @Override
      public String toString() {
        return "" + position + ":" + text;
      }
    };

    // 字母|数字| _ |.组成  返回true
    private static boolean isTypeChar(char c) {
      return Character.isLetterOrDigit(c) || c == '_' || c == '.';
    }

    /**
     * Tokenize the typeInfoString. The rule is simple: all consecutive
     * alphadigits and '_', '.' are in one token, and all other characters are
     * one character per token.
     * 规则是连贯的  字母|数字| _ |.组成
     * tokenize("map<int,string>") should return
     * ["map","<","int",",","string",">"]
     * 拆分成token数组集合
     */
    private static ArrayList<Token> tokenize(String typeInfoString) {
      ArrayList<Token> tokens = new ArrayList<Token>(0);
      int begin = 0;
      int end = 1;
      while (end <= typeInfoString.length()) {//不断重复,一直到最后一个字节
        // last character ends a token?
        if (end == typeInfoString.length()
            || !isTypeChar(typeInfoString.charAt(end - 1))
            || !isTypeChar(typeInfoString.charAt(end))) {//一直去找.直到找到文件最后、该位置不符合标准
          Token t = new Token();//创建一个token
          t.position = begin;
          t.text = typeInfoString.substring(begin, end);
          t.isType = isTypeChar(typeInfoString.charAt(begin));
          tokens.add(t);
          begin = end;
        }
        end++;
      }
      return tokens;
    }

    //对参数进行拆分,参数eg:string:string:string
    public TypeInfoParser(String typeInfoString) {
      this.typeInfoString = typeInfoString;
      typeInfoTokens = tokenize(typeInfoString);
    }

    private final String typeInfoString;//待拆分的原始字符串
    private final ArrayList<Token> typeInfoTokens;//拆分后的token集合
    private ArrayList<TypeInfo> typeInfos;
    private int iToken;//计数器,记录已经解析到哪个token了

    //说明参数不是一个类型,而是由,或者;或者:组成的若干个类型
      //将若干个类型进行解析,组合成一个集合数组返回,用于创建schema的时候使用,表示一组列的类型集合,详细参见BinarySortableSerDe类
    public ArrayList<TypeInfo> parseTypeInfos() {
      typeInfos = new ArrayList<TypeInfo>();
      iToken = 0;
      while (iToken < typeInfoTokens.size()) {
        typeInfos.add(parseType());//解析一个类型
        if (iToken < typeInfoTokens.size()) {//说明后面还有类型要继续解析
          Token separator = typeInfoTokens.get(iToken);
          if (",".equals(separator.text) || ";".equals(separator.text)
              || ":".equals(separator.text)) {
            iToken++;
          } else {
            throw new IllegalArgumentException(
                "Error: ',', ':', or ';' expected at position "
                + separator.position + " from '" + typeInfoString + "' "
                + typeInfoTokens);
          }
        }
      }
      return typeInfos;
    }

    //只是获取,不移动指针
    private Token peek() {
      if (iToken < typeInfoTokens.size()) {
        return typeInfoTokens.get(iToken);
      } else {
        return null;
      }
    }

    private Token expect(String item) {
      return expect(item, null);
    }

    /**
     * 当前的指针指向的token是否与期望的相同,相同则返回该token,不同则抛异常
     * @param item type表示期望该token是类型token,name期望该token的text期望与alternative相同 ,否则text期望与alternative一定相同
     * @param alternative 默认值
     * @return
     */
    private Token expect(String item, String alternative) {
      if (iToken >= typeInfoTokens.size()) {
        throw new IllegalArgumentException("Error: " + item
            + " expected at the end of '" + typeInfoString + "'");
      }
      Token t = typeInfoTokens.get(iToken);
      if (item.equals("type")) {//抽取该类型
          //因此校验token是否是类型,不是则抛异常
          //因为是类型,因此一定是复杂类型和原始类型中能查找到的,如果都查询不到,则抛异常
        if (!serdeConstants.LIST_TYPE_NAME.equals(t.text)
            && !serdeConstants.MAP_TYPE_NAME.equals(t.text)
            && !serdeConstants.STRUCT_TYPE_NAME.equals(t.text)
            && !serdeConstants.UNION_TYPE_NAME.equals(t.text)
            && null == PrimitiveObjectInspectorUtils
            .getTypeEntryFromTypeName(t.text)
            && !t.text.equals(alternative)) {
          throw new IllegalArgumentException("Error: " + item
              + " expected at the position " + t.position + " of '"
              + typeInfoString + "' but '" + t.text + "' is found.");//item是期望类型字符串的第i个位置的内容,但是倍发现的是text
        }
      } else if (item.equals("name")) {//肯定是struc的name
        if (!t.isType && !t.text.equals(alternative)) {//name是属性,而不是具体类型,因此一定不能是类型
          throw new IllegalArgumentException("Error: " + item
              + " expected at the position " + t.position + " of '"
              + typeInfoString + "' but '" + t.text + "' is found.");
        }
      } else {
        if (!item.equals(t.text) && !t.text.equals(alternative)) {//必须和期望的相同
          throw new IllegalArgumentException("Error: " + item
              + " expected at the position " + t.position + " of '"
              + typeInfoString + "' but '" + t.text + "' is found.");
        }
      }
      iToken++;
      return t;
    }

      //解析原始对象的参数,比如decimal(20,2)
    private String[] parseParams() {
      List<String> params = new LinkedList<String>();//存储参数集合

      Token t = peek();
      if (t != null && t.text.equals("(")) {//参数一定是从(开始的
        expect("(");//期待是(

        // checking for null in the for-loop condition prevents null-ptr exception
        // and allows us to fail more gracefully with a parsing error.
        for(t = peek(); (t == null) || !t.text.equals(")"); t = expect(",",")")) {//只要参数不是)结束 并且每次都不断向前期待一个,成立
          params.add(expect("name").text);//期待是一个字符串组成的参数值,此时的name不是struct对应的name属性域的含义,而是具体的参数值的含义
        }
        if (params.size() == 0) {
          throw new IllegalArgumentException(
              "type parameters expected for type string " + typeInfoString);
        }
      }

      return params.toArray(new String[params.size()]);
    }

    //真正开始解析一个复杂对象或者原始对象
    private TypeInfo parseType() {

      Token t = expect("type");

      // Is this a primitive type? 先确定是否是原始类型
      PrimitiveTypeEntry primitiveType = PrimitiveObjectInspectorUtils
          .getTypeEntryFromTypeName(t.text);//获取具体的原始对象
      if (primitiveType != null
          && !primitiveType.primitiveCategory.equals(PrimitiveCategory.UNKNOWN)) {
        if (primitiveType.isParameterized()) {//是有参数的
          primitiveType = primitiveType.addParameters(parseParams());//解析参数
        }
        // If type has qualifiers, the TypeInfo needs them in its type string
        return TypeInfoFactory.getPrimitiveTypeInfo(primitiveType.toString());
      }

      // Is this a list type? 确定是否是list类型
        //schema定义array<TypeInfo>
      if (serdeConstants.LIST_TYPE_NAME.equals(t.text)) {//因为是类型,所以可以判断是否是array类型
        expect("<");
        TypeInfo listElementType = parseType();//因为嵌套的是一个类型,因此递归获取该类型
        expect(">");
        return TypeInfoFactory.getListTypeInfo(listElementType);//创建对应的类型对象
      }

      // Is this a map type?
        //schema定义map<TypeInfo,TypeInfo>
      if (serdeConstants.MAP_TYPE_NAME.equals(t.text)) {
        expect("<");
        TypeInfo mapKeyType = parseType();
        expect(",");
        TypeInfo mapValueType = parseType();
        expect(">");
        return TypeInfoFactory.getMapTypeInfo(mapKeyType, mapValueType);
      }

      // Is this a struct type?
        //schema的定义是struct<name:TypeInfo,name:TypeInfo>
      if (serdeConstants.STRUCT_TYPE_NAME.equals(t.text)) {
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<TypeInfo> fieldTypeInfos = new ArrayList<TypeInfo>();
        boolean first = true;
        do {
          if (first) {//因为是第一个,因此要先解析<
            expect("<");
            first = false;
          } else {
            Token separator = expect(">", ",");//期望的是>或者,
            if (separator.text.equals(">")) {//说明结束了
              // end of struct
              break;
            }
          }
          Token name = expect("name");//期望此时获得的是name
          fieldNames.add(name.text);
          expect(":");
          fieldTypeInfos.add(parseType());
        } while (true);

        return TypeInfoFactory.getStructTypeInfo(fieldNames, fieldTypeInfos);
      }
      // Is this a union type?
        //schema定义uniontype<TypeInfo,TypeInfo>
      if (serdeConstants.UNION_TYPE_NAME.equals(t.text)) {
        List<TypeInfo> objectTypeInfos = new ArrayList<TypeInfo>();
        boolean first = true;
        do {
          if (first) {
            expect("<");
            first = false;
          } else {
            Token separator = expect(">", ",");
            if (separator.text.equals(">")) {
              // end of union
              break;
            }
          }
          objectTypeInfos.add(parseType());
        } while (true);

        return TypeInfoFactory.getUnionTypeInfo(objectTypeInfos);
      }

      throw new RuntimeException("Internal error parsing position "
          + t.position + " of '" + typeInfoString + "'");
    }

      //解析一个原始对象
    public PrimitiveParts parsePrimitiveParts() {
      PrimitiveParts parts = new PrimitiveParts();
      Token t = expect("type");//先获取类型
      parts.typeName = t.text;
      parts.typeParams = parseParams();//解析原始对象的参数,比如decimal(20,2)
      return parts;
    }
  }

  public static class PrimitiveParts {
    public String  typeName;//基础类型
    public String[] typeParams;//基础类型的参数部分
  }

  /**
   * Make some of the TypeInfo parsing available as a utility.
   * 解析类型中基础类型部分,返回基础类型以及参数对象
   */
  public static PrimitiveParts parsePrimitiveParts(String typeInfoString) {
    TypeInfoParser parser = new TypeInfoParser(typeInfoString);
    return parser.parsePrimitiveParts();
  }

  static Map<TypeInfo, ObjectInspector> cachedStandardObjectInspector =
      new ConcurrentHashMap<TypeInfo, ObjectInspector>();

  /**
   * Returns the standard object inspector that can be used to translate an
   * object of that typeInfo to a standard object type.
   */
  public static ObjectInspector getStandardWritableObjectInspectorFromTypeInfo(
      TypeInfo typeInfo) {
    ObjectInspector result = cachedStandardObjectInspector.get(typeInfo);
    if (result == null) {
      switch (typeInfo.getCategory()) {
      case PRIMITIVE: {
        result = PrimitiveObjectInspectorFactory.getPrimitiveWritableObjectInspector(
            (PrimitiveTypeInfo) typeInfo);
        break;
      }
      case LIST: {
        ObjectInspector elementObjectInspector =
            getStandardWritableObjectInspectorFromTypeInfo(((ListTypeInfo) typeInfo)
            .getListElementTypeInfo());
        result = ObjectInspectorFactory
            .getStandardListObjectInspector(elementObjectInspector);
        break;
      }
      case MAP: {
        MapTypeInfo mapTypeInfo = (MapTypeInfo) typeInfo;
        ObjectInspector keyObjectInspector =
            getStandardWritableObjectInspectorFromTypeInfo(mapTypeInfo.getMapKeyTypeInfo());
        ObjectInspector valueObjectInspector =
            getStandardWritableObjectInspectorFromTypeInfo(mapTypeInfo.getMapValueTypeInfo());
        result = ObjectInspectorFactory.getStandardMapObjectInspector(
            keyObjectInspector, valueObjectInspector);
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
              .add(getStandardWritableObjectInspectorFromTypeInfo(fieldTypeInfos
              .get(i)));
        }
        result = ObjectInspectorFactory.getStandardStructObjectInspector(
            fieldNames, fieldObjectInspectors);
        break;
      }
      case UNION: {
        UnionTypeInfo unionTypeInfo = (UnionTypeInfo) typeInfo;
        List<TypeInfo> objectTypeInfos = unionTypeInfo
            .getAllUnionObjectTypeInfos();
        List<ObjectInspector> fieldObjectInspectors =
          new ArrayList<ObjectInspector>(objectTypeInfos.size());
        for (int i = 0; i < objectTypeInfos.size(); i++) {
          fieldObjectInspectors
              .add(getStandardWritableObjectInspectorFromTypeInfo(objectTypeInfos
              .get(i)));
        }
        result = ObjectInspectorFactory.getStandardUnionObjectInspector(
            fieldObjectInspectors);
        break;
      }

      default: {
        result = null;
      }
      }
      cachedStandardObjectInspector.put(typeInfo, result);
    }
    return result;
  }

  static Map<TypeInfo, ObjectInspector> cachedStandardJavaObjectInspector =
      new ConcurrentHashMap<TypeInfo, ObjectInspector>();

  /**
   * Returns the standard object inspector that can be used to translate an
   * object of that typeInfo to a standard object type.
   */
  public static ObjectInspector getStandardJavaObjectInspectorFromTypeInfo(
      TypeInfo typeInfo) {
    ObjectInspector result = cachedStandardJavaObjectInspector.get(typeInfo);
    if (result == null) {
      switch (typeInfo.getCategory()) {
      case PRIMITIVE: {
        // NOTE: we use JavaPrimitiveObjectInspector instead of
        // StandardPrimitiveObjectInspector
        result = PrimitiveObjectInspectorFactory
            .getPrimitiveJavaObjectInspector((PrimitiveTypeInfo) typeInfo);
        break;
      }
      case LIST: {
        ObjectInspector elementObjectInspector =
            getStandardJavaObjectInspectorFromTypeInfo(((ListTypeInfo) typeInfo)
            .getListElementTypeInfo());
        result = ObjectInspectorFactory
            .getStandardListObjectInspector(elementObjectInspector);
        break;
      }
      case MAP: {
        MapTypeInfo mapTypeInfo = (MapTypeInfo) typeInfo;
        ObjectInspector keyObjectInspector = getStandardJavaObjectInspectorFromTypeInfo(mapTypeInfo
            .getMapKeyTypeInfo());
        ObjectInspector valueObjectInspector =
            getStandardJavaObjectInspectorFromTypeInfo(mapTypeInfo.getMapValueTypeInfo());
        result = ObjectInspectorFactory.getStandardMapObjectInspector(
            keyObjectInspector, valueObjectInspector);
        break;
      }
      case STRUCT: {
        StructTypeInfo strucTypeInfo = (StructTypeInfo) typeInfo;
        List<String> fieldNames = strucTypeInfo.getAllStructFieldNames();
        List<TypeInfo> fieldTypeInfos = strucTypeInfo
            .getAllStructFieldTypeInfos();
        List<ObjectInspector> fieldObjectInspectors = new ArrayList<ObjectInspector>(
            fieldTypeInfos.size());
        for (int i = 0; i < fieldTypeInfos.size(); i++) {
          fieldObjectInspectors
              .add(getStandardJavaObjectInspectorFromTypeInfo(fieldTypeInfos
              .get(i)));
        }
        result = ObjectInspectorFactory.getStandardStructObjectInspector(
            fieldNames, fieldObjectInspectors);
        break;
      }
      case UNION: {
        UnionTypeInfo unionTypeInfo = (UnionTypeInfo) typeInfo;
        List<TypeInfo> objectTypeInfos = unionTypeInfo
            .getAllUnionObjectTypeInfos();
        List<ObjectInspector> fieldObjectInspectors =
          new ArrayList<ObjectInspector>(objectTypeInfos.size());
        for (int i = 0; i < objectTypeInfos.size(); i++) {
          fieldObjectInspectors
              .add(getStandardJavaObjectInspectorFromTypeInfo(objectTypeInfos
              .get(i)));
        }
        result = ObjectInspectorFactory.getStandardUnionObjectInspector(
            fieldObjectInspectors);
        break;
      }
     default: {
        result = null;
      }
      }
      cachedStandardJavaObjectInspector.put(typeInfo, result);
    }
    return result;
  }

  /**
   * Get the TypeInfo object from the ObjectInspector object by recursively
   * going into the ObjectInspector structure.
   */
  public static TypeInfo getTypeInfoFromObjectInspector(ObjectInspector oi) {
    // OPTIMIZATION for later.
    // if (oi instanceof TypeInfoBasedObjectInspector) {
    // TypeInfoBasedObjectInspector typeInfoBasedObjectInspector =
    // (ObjectInspector)oi;
    // return typeInfoBasedObjectInspector.getTypeInfo();
    // }
    if (oi == null) {
      return null;
    }

    // Recursively going into ObjectInspector structure
    TypeInfo result = null;
    switch (oi.getCategory()) {
    case PRIMITIVE: {
      PrimitiveObjectInspector poi = (PrimitiveObjectInspector) oi;
      result = TypeInfoFactory.getPrimitiveTypeInfo(poi.getTypeName());
      break;
    }
    case LIST: {
      ListObjectInspector loi = (ListObjectInspector) oi;
      result = TypeInfoFactory
          .getListTypeInfo(getTypeInfoFromObjectInspector(loi
          .getListElementObjectInspector()));
      break;
    }
    case MAP: {
      MapObjectInspector moi = (MapObjectInspector) oi;
      result = TypeInfoFactory.getMapTypeInfo(
          getTypeInfoFromObjectInspector(moi.getMapKeyObjectInspector()),
          getTypeInfoFromObjectInspector(moi.getMapValueObjectInspector()));
      break;
    }
    case STRUCT: {
      StructObjectInspector soi = (StructObjectInspector) oi;
      List<? extends StructField> fields = soi.getAllStructFieldRefs();
      List<String> fieldNames = new ArrayList<String>(fields.size());
      List<TypeInfo> fieldTypeInfos = new ArrayList<TypeInfo>(fields.size());
      for (StructField f : fields) {
        fieldNames.add(f.getFieldName());
        fieldTypeInfos.add(getTypeInfoFromObjectInspector(f
            .getFieldObjectInspector()));
      }
      result = TypeInfoFactory.getStructTypeInfo(fieldNames, fieldTypeInfos);
      break;
    }
    case UNION: {
      UnionObjectInspector uoi = (UnionObjectInspector) oi;
      List<TypeInfo> objectTypeInfos = new ArrayList<TypeInfo>();
      for (ObjectInspector eoi : uoi.getObjectInspectors()) {
        objectTypeInfos.add(getTypeInfoFromObjectInspector(eoi));
      }
      result = TypeInfoFactory.getUnionTypeInfo(objectTypeInfos);
      break;
    }
    default: {
      throw new RuntimeException("Unknown ObjectInspector category!");
    }
    }
    return result;
  }

  //参数eg:string:string:string
  //解析参数,每一个参数都是一个属性的类型信息
  public static ArrayList<TypeInfo> getTypeInfosFromTypeString(String typeString) {
    TypeInfoParser parser = new TypeInfoParser(typeString);
    return parser.parseTypeInfos();
  }

  public static TypeInfo getTypeInfoFromTypeString(String typeString) {
    TypeInfoParser parser = new TypeInfoParser(typeString);
    return parser.parseTypeInfos().get(0);
  }

  /**
   * Given two types, determine whether conversion needs to occur to compare the two types.
   * This is needed for cases like varchar, where the TypeInfo for varchar(10) != varchar(5),
   * but there would be no need to have to convert to compare these values.
   * @param typeA
   * @param typeB
   * @return
   */
  public static boolean isConversionRequiredForComparison(TypeInfo typeA, TypeInfo typeB) {
    if (typeA == typeB) {
      return false;
    }
    if (TypeInfoUtils.doPrimitiveCategoriesMatch(typeA,  typeB)) {
      return false;
    }
    return true;
  }

  /**
   * Return the character length of the type
   * @param typeInfo
   * @return
   */
  public static int getCharacterLengthForType(PrimitiveTypeInfo typeInfo) {
    switch (typeInfo.getPrimitiveCategory()) {
      case STRING:
        return HiveVarchar.MAX_VARCHAR_LENGTH;
      case VARCHAR:
        VarcharTypeParams varcharParams = (VarcharTypeParams) typeInfo.getTypeParams();
        if (varcharParams == null) {
          throw new RuntimeException("varchar type used without type params");
        }
        return varcharParams.getLength();
      default:
        return 0;
    }
  }
}
