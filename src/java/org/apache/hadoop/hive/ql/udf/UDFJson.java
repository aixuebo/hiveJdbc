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

package org.apache.hadoop.hive.ql.udf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/**
 * UDFJson.
 *
 例如:提取某一个json字符串中的具体信息,根据path去提取
 {"store":
  {"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],
   "bicycle":{"price":19.95,"color":"red"}
  },
 "email":"amy@only_for_json_udf_test.net",
 "owner":"amy"
}
+----+
The fields of the json object can be extracted using these queries:
hive> SELECT get_json_object(src_json.json, '$.owner') FROM src_json;
amy
 
hive> SELECT get_json_object(src_json.json, '$.store.fruit\[0]') FROM src_json;
{"weight":8,"type":"apple"}
 
hive> SELECT get_json_object(src_json.json, '$.non_exist_key') FROM src_json;
NULL


path规则
1.$表示根目录
2..表示子目录
3.[]表示一个数组
 */
@Description(name = "get_json_object",
    value = "_FUNC_(json_txt, path) - Extract a json object from path ",
    extended = "Extract json object from a json string based on json path "
    + "specified, and return json string of the extracted json object. It "
    + "will return null if the input json string is invalid.\n"
    + "A limited version of JSONPath supported:\n"
    + "  $   : Root object\n"
    + "  .   : Child operator\n"
    + "  []  : Subscript operator for array\n"
    + "  *   : Wildcard for []\n"
    + "Syntax not supported that's worth noticing:\n"
    + "  ''  : Zero length string as key\n"
    + "  ..  : Recursive descent\n"
    + "  &amp;#064;   : Current object/element\n"
    + "  ()  : Script expression\n"
    + "  ?() : Filter (script) expression.\n"
    + "  [,] : Union operator\n"
    + "  [start:end:step] : array slice operator\n")
public class UDFJson extends UDF {
	
  //以字母、数字、_、-、:开头的都匹配
  private final Pattern patternKey = Pattern.compile("^([a-zA-Z0-9_\\-\\:\\s]+).*");
  //以[*] 或者[数组]组成的都匹配,即匹配list[2]获取下标为2的元素,或者list[*]获取全部元素
  private final Pattern patternIndex = Pattern.compile("\\[([0-9]+|\\*)\\]");

  private static final JsonFactory JSON_FACTORY = new JsonFactory();
  static {
    // Allows for unescaped ASCII control characters in JSON values
    JSON_FACTORY.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
  }
  
  //将json字符串转换成Map形式的对象
  private static final ObjectMapper MAPPER = new ObjectMapper(JSON_FACTORY);
  private static final JavaType MAP_TYPE = TypeFactory.fromClass(Map.class);

  // An LRU cache using a linked hash map
  static class HashCache<K, V> extends LinkedHashMap<K, V> {

    private static final int CACHE_SIZE = 16;//缓存大小,只保留这些个数据
    private static final int INIT_SIZE = 32;
    private static final float LOAD_FACTOR = 0.6f;

    HashCache() {
      super(INIT_SIZE, LOAD_FACTOR);
    }

    private static final long serialVersionUID = 1;

    /**
     * true表示移除老的数据
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
      return size() > CACHE_SIZE;
    }

  }

  /**
   * 将json字符串转换成Map形式的对象
   * key是json字符串,value是Map对象
   */
  static Map<String, Object> extractObjectCache = new HashCache<String, Object>();
  
  //缓存path与path按照.拆分后的数组
  static Map<String, String[]> pathExprCache = new HashCache<String, String[]>();
  
  static Map<String, ArrayList<String>> indexListCache =
      new HashCache<String, ArrayList<String>>();
  
  
  static Map<String, String> mKeyGroup1Cache = new HashCache<String, String>();
  
  //key是path字符串,value是该path是否语法正确,true表示正确
  static Map<String, Boolean> mKeyMatchesCache = new HashCache<String, Boolean>();

  Text result = new Text();

  public UDFJson() {
  }

  /**
   * Extract json object from a json string based on json path specified, and
   * return json string of the extracted json object. It will return null if the
   * input json string is invalid.
   *
   * A limited version of JSONPath supported: $ : Root object . : Child operator
   * [] : Subscript operator for array * : Wildcard for []
   *
   * Syntax not supported that's worth noticing: '' : Zero length string as key
   * .. : Recursive descent &amp;#064; : Current object/element () : Script
   * expression ?() : Filter (script) expression. [,] : Union operator
   * [start:end:step] : array slice operator
   *
   * @param jsonString
   *          the json string.
   * @param pathString
   *          the json path expression.
   * @return json string or null when an error happens.
   * 从json字符串中按照path路径抽取对应的字符串值
   */
  public Text evaluate(String jsonString, String pathString) {

    if (jsonString == null || jsonString == "" || pathString == null
        || pathString == "") {
      return null;
    }

    // Cache pathExpr 先从缓存中获取path对应的数组
    String[] pathExpr = pathExprCache.get(pathString);
    if (pathExpr == null) {
      pathExpr = pathString.split("\\.", -1);
      pathExprCache.put(pathString, pathExpr);
    }

    //校验数组一定以$开头,即从根目录开始获取json值
    if (!pathExpr[0].equalsIgnoreCase("$")) {
      return null;
    }
    
    // Cache extractObject 将json字符串转换成Map形式
    Object extractObject = extractObjectCache.get(jsonString);
    if (extractObject == null) {
      try {
        extractObject = MAPPER.readValue(jsonString, MAP_TYPE);
      } catch (Exception e) {
        return null;
      }
      extractObjectCache.put(jsonString, extractObject);
    }
    for (int i = 1; i < pathExpr.length; i++) {
      if (extractObject == null) {//如果json字符串对应的不是map对象的json,则返回null,说明json字符串有问题
          return null;
      }
      
      //从map的json对象中抽取每一个子集
      extractObject = extract(extractObject, pathExpr[i]);
    }
    
    //如果最后结果是map或者list,则将其序列化成字符串
    if (extractObject instanceof Map || extractObject instanceof List) {
      try {
        result.set(MAPPER.writeValueAsString(extractObject));
      } catch (Exception e) {
        return null;
      }
    } else if (extractObject != null) {//获取最终对象的字符串形式
      result.set(extractObject.toString());
    } else {
      return null;
    }
    return result;
  }

  /**
   * 从map的json对象中抽取每一个子集
   * @param json json对象,根节点是map,因此内部有可能任何类型都可以
   * @param path 获取某一个子集
   * @return 返回字节对应的json对象
   */
  private Object extract(Object json, String path) {

    // Cache patternkey.matcher(path).matches()
    Matcher mKey = null;
    //校验path是否格式、语法正确,不正确则返回null
    //为了防止每次都校验path,可以将path的结果缓存起来
    Boolean mKeyMatches = mKeyMatchesCache.get(path);
    if (mKeyMatches == null) {
      mKey = patternKey.matcher(path);
      mKeyMatches = mKey.matches() ? Boolean.TRUE : Boolean.FALSE;
      mKeyMatchesCache.put(path, mKeyMatches);
    }
    if (!mKeyMatches.booleanValue()) {
      return null;
    }

    // Cache mkey.group(1)
    String mKeyGroup1 = mKeyGroup1Cache.get(path);
    if (mKeyGroup1 == null) {
      if (mKey == null) {
        mKey = patternKey.matcher(path);
      }
      mKeyGroup1 = mKey.group(1);
      mKeyGroup1Cache.put(path, mKeyGroup1);
    }
    
    //通过属性name的方式获取json的子属性
    json = extract_json_withkey(json, mKeyGroup1);

    //通过下标获取数据信息
    // Cache indexList
    ArrayList<String> indexList = indexListCache.get(path);
    if (indexList == null) {
      Matcher mIndex = patternIndex.matcher(path);
      indexList = new ArrayList<String>();
      while (mIndex.find()) {
        indexList.add(mIndex.group(1));
      }
      indexListCache.put(path, indexList);
    }

    if (indexList.size() > 0) {
      json = extract_json_withindex(json, indexList);
    }

    return json;
  }

  List<Object> jsonList = new ArrayList<Object>();

  /**
   * 通过索引的方式获取json对象的某一个属性
   */
  @SuppressWarnings("unchecked")
  private Object extract_json_withindex(Object json, ArrayList<String> indexList) {

    jsonList.clear();
    jsonList.add(json);
    Iterator<String> itr = indexList.iterator();
    while (itr.hasNext()) {
      String index = itr.next();
      List<Object> tmp_jsonList = new ArrayList<Object>();
      if (index.equalsIgnoreCase("*")) {
        for (int i = 0; i < jsonList.size(); i++) {
          Object array = jsonList.get(i);
          if (array instanceof List) {
            for (int j = 0; j < ((List<Object>)array).size(); j++) {
              tmp_jsonList.add(((List<Object>)array).get(j));
            }
          }
        }
        jsonList = tmp_jsonList;
      } else {
        for (int i = 0; i < (jsonList).size(); i++) {
          Object array = jsonList.get(i);
          int indexValue = Integer.parseInt(index);
          if (!(array instanceof List)) {
            continue;
          }
          if (indexValue >= ((List<Object>)array).size()) {
            return null;
          }
          tmp_jsonList.add(((List<Object>)array).get(indexValue));
          jsonList = tmp_jsonList;
        }
      }
    }
    if (jsonList.isEmpty()) {
      return null;
    }
    return (jsonList.size() > 1) ? new ArrayList<Object>(jsonList) : jsonList.get(0);
  }

  /**
   * 从json对象中获取某一个path属性,返回对应的json对象
   */
  @SuppressWarnings("unchecked")
  private Object extract_json_withkey(Object json, String path) {
    if (json instanceof List) {
      List<Object> jsonArray = new ArrayList<Object>();
      for (int i = 0; i < ((List<Object>) json).size(); i++) {
        Object json_elem = ((List<Object>) json).get(i);
        Object json_obj = null;
        if (json_elem instanceof Map) {
          json_obj = ((Map<String, Object>) json_elem).get(path);
        } else {
          continue;
        }
        if (json_obj instanceof List) {
          for (int j = 0; j < ((List<Object>) json_obj).size(); j++) {
            jsonArray.add(((List<Object>) json_obj).get(j));
          }
        } else if (json_obj != null) {
          jsonArray.add(json_obj);
        }
      }
      return (jsonArray.size() == 0) ? null : jsonArray;
    } else if (json instanceof Map) {
      return ((Map<String, Object>) json).get(path);
    } else {
      return null;
    }
  }
}
