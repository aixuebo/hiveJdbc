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
package org.apache.hadoop.hive.common.type;


public abstract class HiveBaseChar {
  protected String value;
  protected int characterLength = -1;

  protected HiveBaseChar() {
  }

  /**
   * Sets the string value to a new value, obeying the max length defined for this object.
   * @param val new value
   */
  public void setValue(String val, int maxLength) {
    characterLength = -1;
    //对value进行最大unicode字符限制处理
    value = HiveBaseChar.enforceMaxLength(val, maxLength);
  }

  public void setValue(HiveBaseChar val, int maxLength) {
    if ((maxLength < 0)
        || (val.characterLength > 0 && val.characterLength <= maxLength)) {
      // No length enforcement required, or source length is less than max length.
      // We can copy the source value as-is.
      value = val.value;
      this.characterLength = val.characterLength;
    } else {
      setValue(val.value, maxLength);
    }
  }

  //截取val最多允许maxLength个unicode字符
  public static String enforceMaxLength(String val, int maxLength) {
    String value = val;

    if (maxLength > 0) {
      int valLength = val.codePointCount(0, val.length());//计算val的unicode字符个数
      if (valLength > maxLength) {
        // Truncate the excess trailing spaces to fit the character length.
        // Also make sure we take supplementary chars into account.
        value = val.substring(0, val.offsetByCodePoints(0, maxLength));
      }
    }
    return value;
  }

  public String getValue() {
    return value;
  }

  /**
   * 计算当前value的unicode字符个数
   */
  public int getCharacterLength() {
    if (characterLength < 0) {
      characterLength = value.codePointCount(0, value.length());
    }
    return characterLength;
  }
}
