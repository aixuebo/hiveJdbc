/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.hadoop.hive.serde;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class serdeConstants {

  public static final String SERIALIZATION_LIB = "serialization.lib";

  public static final String SERIALIZATION_CLASS = "serialization.class";

  public static final String SERIALIZATION_FORMAT = "serialization.format";//拆分每一个属性的配置,field.delim的默认值,默认是001

  public static final String SERIALIZATION_DDL = "serialization.ddl";

  public static final String SERIALIZATION_NULL_FORMAT = "serialization.null.format";//空值的配置文件中的key输出,默认是\\N

  //lastColumnTakesRestString在配置文件中的key
  /**
字段意义说明
CREATE TABLE escape2 (id STRING, name STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '"';

LOAD DATA LOCAL INPATH '/home/tianzhao/book/escape2.txt' 
OVERWRITE INTO TABLE escape2; 

escape2.txt 的内容是： 
Joe"2"3333"44 
Hank"2"3333"44 
实际数据比表的字段要多。 

select * from escape2; 
Joe 2 
Hank 2 
ALTER TABLE escape2 SET SERDEPROPERTIES ('serialization.last.column.takes.rest' = 'true'); 
serialization.last.column.takes.rest 的意思是最后一个字段的内容是否包含那些多余的数据： 
select * from escape2; 
Joe 2"3333"44 
Hank 2"3333"44 
   */
  public static final String SERIALIZATION_LAST_COLUMN_TAKES_REST = "serialization.last.column.takes.rest";//该值是true或者其他,只有true才是最关键的属性,其他都认为是false

  public static final String SERIALIZATION_SORT_ORDER = "serialization.sort.order";

  public static final String SERIALIZATION_USE_JSON_OBJECTS = "serialization.use.json.object";

  public static final String FIELD_DELIM = "field.delim";//每一组field属性的拆分配置key,默认是001

  public static final String COLLECTION_DELIM = "colelction.delim";//集合的拆分配置key,默认值是002

  public static final String LINE_DELIM = "line.delim";//行拆分配置key,默认是\n

  public static final String MAPKEY_DELIM = "mapkey.delim";//map的拆分配置key,默认值是003

  public static final String QUOTE_CHAR = "quote.delim";

  public static final String ESCAPE_CHAR = "escape.delim";//转义字符的key,value是127以内的数字,或者字符串就获取第一个char作为转义字符,如果该key对应的value不是空,则说明需要转义

  //hive支持的类型
  public static final String VOID_TYPE_NAME = "void";

  public static final String BOOLEAN_TYPE_NAME = "boolean";

  public static final String TINYINT_TYPE_NAME = "tinyint";

  public static final String SMALLINT_TYPE_NAME = "smallint";

  public static final String INT_TYPE_NAME = "int";

  public static final String BIGINT_TYPE_NAME = "bigint";

  public static final String FLOAT_TYPE_NAME = "float";

  public static final String DOUBLE_TYPE_NAME = "double";

  public static final String STRING_TYPE_NAME = "string";

  public static final String CHAR_TYPE_NAME = "char";

  public static final String VARCHAR_TYPE_NAME = "varchar";

  public static final String DATE_TYPE_NAME = "date";

  public static final String DATETIME_TYPE_NAME = "datetime";

  public static final String TIMESTAMP_TYPE_NAME = "timestamp";

  public static final String DECIMAL_TYPE_NAME = "decimal";

  public static final String BINARY_TYPE_NAME = "binary";

    /**
     schema的定义是struct<name:TypeInfo,name:TypeInfo>
     schema定义map<TypeInfo,TypeInfo>
     schema定义array<TypeInfo>
     schema定义uniontype<TypeInfo,TypeInfo>
     原始类型定义TypeInfo--比如decimal(20,1)
     */
  public static final String LIST_TYPE_NAME = "array";

  public static final String MAP_TYPE_NAME = "map";

  public static final String STRUCT_TYPE_NAME = "struct";

  public static final String UNION_TYPE_NAME = "uniontype";

  //解析属性集合的key,该key对应的value值是按照逗号拆分的
  public static final String LIST_COLUMNS = "columns";

  //解析属性的类型集合的key,该key对应的value值是按照:号拆分的
  public static final String LIST_COLUMN_TYPES = "columns.types";

  public static final Set<String> PrimitiveTypes = new HashSet<String>();
  static {
    PrimitiveTypes.add("void");
    PrimitiveTypes.add("boolean");
    PrimitiveTypes.add("tinyint");
    PrimitiveTypes.add("smallint");
    PrimitiveTypes.add("int");
    PrimitiveTypes.add("bigint");
    PrimitiveTypes.add("float");
    PrimitiveTypes.add("double");
    PrimitiveTypes.add("string");
    PrimitiveTypes.add("varchar");
    PrimitiveTypes.add("char");
    PrimitiveTypes.add("date");
    PrimitiveTypes.add("datetime");
    PrimitiveTypes.add("timestamp");
    PrimitiveTypes.add("decimal");
    PrimitiveTypes.add("binary");
  }

  public static final Set<String> CollectionTypes = new HashSet<String>();
  static {
    CollectionTypes.add("array");
    CollectionTypes.add("map");
  }

}
