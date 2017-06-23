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

package org.apache.hive.service.cli;

import java.sql.DatabaseMetaData;

import org.apache.hive.service.cli.thrift.TTypeId;

/**
 * Type.
 * 表示列类型
 */
public enum Type {
  NULL_TYPE("VOID",
      java.sql.Types.NULL,
      TTypeId.NULL_TYPE),
  BOOLEAN_TYPE("BOOLEAN",//hive中定义的类型
      java.sql.Types.BOOLEAN,//sql接口定义的列类型
      TTypeId.BOOLEAN_TYPE),//thrift中定义的列类型
  TINYINT_TYPE("TINYINT",
      java.sql.Types.TINYINT,
      TTypeId.TINYINT_TYPE),
  SMALLINT_TYPE("SMALLINT",
      java.sql.Types.SMALLINT,
      TTypeId.SMALLINT_TYPE),
  INT_TYPE("INT",
      java.sql.Types.INTEGER,
      TTypeId.INT_TYPE),
  BIGINT_TYPE("BIGINT",
      java.sql.Types.BIGINT,
      TTypeId.BIGINT_TYPE),
  FLOAT_TYPE("FLOAT",
      java.sql.Types.FLOAT,
      TTypeId.FLOAT_TYPE),
  DOUBLE_TYPE("DOUBLE",
      java.sql.Types.DOUBLE,
      TTypeId.DOUBLE_TYPE),
  STRING_TYPE("STRING",
      java.sql.Types.VARCHAR,
      TTypeId.STRING_TYPE),
  VARCHAR_TYPE("VARCHAR",
      java.sql.Types.VARCHAR,
      TTypeId.VARCHAR_TYPE,
      true, false, false),
  DATE_TYPE("DATE",
      java.sql.Types.DATE,
      TTypeId.DATE_TYPE),
  TIMESTAMP_TYPE("TIMESTAMP",
      java.sql.Types.TIMESTAMP,
      TTypeId.TIMESTAMP_TYPE),
  BINARY_TYPE("BINARY",
      java.sql.Types.BINARY,
      TTypeId.BINARY_TYPE),
  DECIMAL_TYPE("DECIMAL",
      java.sql.Types.DECIMAL,
      TTypeId.DECIMAL_TYPE,
      true, false, false),
  ARRAY_TYPE("ARRAY",
      java.sql.Types.VARCHAR,
      TTypeId.STRING_TYPE,
      true, true),
  MAP_TYPE("MAP",
      java.sql.Types.VARCHAR,
      TTypeId.STRING_TYPE,
      true, true),
  STRUCT_TYPE("STRUCT",
      java.sql.Types.VARCHAR,//sql和thrift中都使用json表示,因此是String类型
      TTypeId.STRING_TYPE,
      true, false),
  UNION_TYPE("UNIONTYPE",
      java.sql.Types.VARCHAR,
      TTypeId.STRING_TYPE,
      true, false),
  USER_DEFINED_TYPE("USER_DEFINED",
      java.sql.Types.VARCHAR,
      TTypeId.STRING_TYPE,
      true, false);

  private final String name;//自己定义的类型
  private final TTypeId tType;//thrift中定义的类型
  private final int javaSQLType;//sql接口定义的类型
  private final boolean isQualified;//DECIMAL和verchar时候是true,含义应该是是否需要参数的意思,因为这两个类型需要参数修饰
  private final boolean isComplex;//是否是复杂类型,true表示是复杂类型,比如Map struct等类型
  private final boolean isCollection;//是否是集合类型,只有ARRAY和MAP是集合类型,即该字段可以存储值

  Type(String name, int javaSQLType, TTypeId tType, boolean isQualified, boolean isComplex, boolean isCollection) {
    this.name = name;
    this.javaSQLType = javaSQLType;
    this.tType = tType;
    this.isQualified = isQualified;
    this.isComplex = isComplex;
    this.isCollection = isCollection;
  }

  Type(String name, int javaSQLType, TTypeId tType, boolean isComplex, boolean isCollection) {
    this(name, javaSQLType, tType, false, isComplex, isCollection);
  }

  Type(String name, int javaSqlType, TTypeId tType) {
    this(name, javaSqlType, tType, false, false, false);
  }

  //是否是简单类型
  public boolean isPrimitiveType() {
    return !isComplex;
  }

  public boolean isQualifiedType() {
    return isQualified;
  }

  //是否是复杂类型
  public boolean isComplexType() {
    return isComplex;
  }

  public boolean isCollectionType() {
    return isCollection;
  }

    //通过thrift类型找类型
  public static Type getType(TTypeId tType) {
    for (Type type : values()) {
      if (tType.equals(type.tType)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unregonized Thrift TTypeId value: " + tType);
  }

    //通过hive的类型找类型
  public static Type getType(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Invalid type name: null");
    }
    for (Type type : values()) {
      if (name.equalsIgnoreCase(type.name)) {
        return type;
      } else if (type.isQualifiedType() || type.isComplexType()) {//因为需要参数,因此只比较开头部分即可
        if (name.toUpperCase().startsWith(type.name)) {
            return type;
        }
      }
    }
    throw new IllegalArgumentException("Unrecognized type name: " + name);
  }

  /**
   * Radix for this type (typically either 2 or 10)
   * Null is returned for data types where this is not applicable.
   */
  public Integer getNumPrecRadix() {
    switch (this) {
    case TINYINT_TYPE:
    case SMALLINT_TYPE:
    case INT_TYPE:
    case BIGINT_TYPE:
      return 10;
    case FLOAT_TYPE:
    case DOUBLE_TYPE:
      return 2;
    default:
      // everything else (including boolean and string) is null
      return null;
    }
  }

  /**
   * The number of fractional digits for this type.
   * Null is returned for data types where this is not applicable.
   */
  public Integer getDecimalDigits() {
    switch (this) {
    case BOOLEAN_TYPE:
    case TINYINT_TYPE:
    case SMALLINT_TYPE:
    case INT_TYPE:
    case BIGINT_TYPE:
      return 0;
    case FLOAT_TYPE:
      return 7;
    case DOUBLE_TYPE:
      return 15;
    default:
      return null;
    }
  }

  /**
   * Maximum precision for numeric types.
   * Returns null for non-numeric types.
   * @return
   */
  public Integer getPrecision() {
    switch (this) {
    case TINYINT_TYPE:
      return 3;
    case SMALLINT_TYPE:
      return 5;
    case INT_TYPE:
      return 10;
    case BIGINT_TYPE:
      return 19;
    case FLOAT_TYPE:
      return 7;
    case DOUBLE_TYPE:
      return 15;
    default:
      return null;
    }
  }

  /**
   * Scale for this type.
   */
  public Integer getScale() {
    switch (this) {
    case BOOLEAN_TYPE:
    case STRING_TYPE:
    case DATE_TYPE:
    case TIMESTAMP_TYPE:
    case TINYINT_TYPE:
    case SMALLINT_TYPE:
    case INT_TYPE:
    case BIGINT_TYPE:
      return 0;
    case FLOAT_TYPE:
      return 7;
    case DOUBLE_TYPE:
      return 15;
    case DECIMAL_TYPE:
      return Integer.MAX_VALUE;
    default:
      return null;
    }
  }

  /**
   * The column size for this type.
   * For numeric data this is the maximum precision.
   * For character data this is the length in characters.
   * For datetime types this is the length in characters of the String representation
   * (assuming the maximum allowed precision of the fractional seconds component).
   * For binary data this is the length in bytes.
   * Null is returned for for data types where the column size is not applicable.
   */
  public Integer getColumnSize() {
    if (isNumericType()) {
      return getPrecision();
    }
    switch (this) {
    case STRING_TYPE:
    case BINARY_TYPE:
      return Integer.MAX_VALUE;
    case DATE_TYPE:
      return 10;
    case TIMESTAMP_TYPE:
      return 30;
    default:
      return null;
    }
  }

  public boolean isNumericType() {
    switch (this) {
    case TINYINT_TYPE:
    case SMALLINT_TYPE:
    case INT_TYPE:
    case BIGINT_TYPE:
    case FLOAT_TYPE:
    case DOUBLE_TYPE:
    case DECIMAL_TYPE:
      return true;
    default:
      return false;
    }
  }

  /**
   * Prefix used to quote a literal of this type (may be null)
   */
  public String getLiteralPrefix() {
    return null;
  }

  /**
   * Suffix used to quote a literal of this type (may be null)
   * @return
   */
  public String getLiteralSuffix() {
    return null;
  }

  /**
   * Can you use NULL for this type?
   * @return
   * DatabaseMetaData.typeNoNulls - does not allow NULL values
   * DatabaseMetaData.typeNullable - allows NULL values
   * DatabaseMetaData.typeNullableUnknown - nullability unknown
   */
  public Short getNullable() {
    // All Hive types are nullable
    return DatabaseMetaData.typeNullable;
  }

  /**
   * Is the type case sensitive?
   * @return
   */
  public Boolean isCaseSensitive() {
    switch (this) {
    case STRING_TYPE:
      return true;
    default:
      return false;
    }
  }

  /**
   * Parameters used in creating the type (may be null)
   * @return
   */
  public String getCreateParams() {
    return null;
  }

  /**
   * Can you use WHERE based on this type?
   * @return
   * DatabaseMetaData.typePredNone - No support
   * DatabaseMetaData.typePredChar - Only support with WHERE .. LIKE
   * DatabaseMetaData.typePredBasic - Supported except for WHERE .. LIKE
   * DatabaseMetaData.typeSearchable - Supported for all WHERE ..
   */
  public Short getSearchable() {
    if (isPrimitiveType()) {
      return DatabaseMetaData.typeSearchable;
    }
    return DatabaseMetaData.typePredNone;
  }

  /**
   * Is this type unsigned?
   * @return
   */
  public Boolean isUnsignedAttribute() {
    if (isNumericType()) {
      return false;
    }
    return true;
  }

  /**
   * Can this type represent money?
   * @return
   */
  public Boolean isFixedPrecScale() {
    return false;
  }

  /**
   * Can this type be used for an auto-increment value?
   * @return
   */
  public Boolean isAutoIncrement() {
    return false;
  }

  /**
   * Localized version of type name (may be null).
   * @return
   */
  public String getLocalizedName() {
    return null;
  }

  /**
   * Minimum scale supported for this type
   * @return
   */
  public Short getMinimumScale() {
    return 0;
  }

  /**
   * Maximum scale supported for this type
   * @return
   */
  public Short getMaximumScale() {
    return 0;
  }

  public TTypeId toTType() {
    return tType;
  }

  public int toJavaSQLType() {
    return javaSQLType;
  }

  public String getName() {
    return name;
  }
}
