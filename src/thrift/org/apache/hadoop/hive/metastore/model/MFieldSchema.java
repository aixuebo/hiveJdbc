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

/**
 * 
 */
package org.apache.hadoop.hive.metastore.model;

/**
 * Represent a column or a type of a table or object
 * 标示table表格的一个列信息
 */
public class MFieldSchema {
  private String name;//属性名字
  private String type;//属性类型
  private String comment;//备注
  public MFieldSchema() {}

  /**
   * @param comment
   * @param name
   * @param type
   */
  public MFieldSchema(String name, String type, String comment) {
    this.comment = comment;
    this.name = name;
    this.type = type;
  }
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @return the comment
   */
  public String getComment() {
    return comment;
  }
  /**
   * @param comment the comment to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }
  /**
   * @return the type
   */
  public String getType() {
    return type;
  }
  /**
   * @param field the type to set
   */
  public void setType(String field) {
    this.type = field;
  }
  
}
