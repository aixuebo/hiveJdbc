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

package org.apache.hadoop.hive.serde2.objectinspector;

/**
 * StructField is an empty interface.
 * 
 * Classes implementing this interface are considered to represent a field of a
 * struct for this serde package.
 * ����һ������
 */
public interface StructField {

  /**
   * Get the name of the field. The name should be always in lower-case.
   * �������ֵ�Сд
   */
  String getFieldName();

  /**
   * Get the ObjectInspector for the field.
   * ���Զ�Ӧ������Type����
   */
  ObjectInspector getFieldObjectInspector();

  /**
   * Get the comment for the field. May be null if no comment provided.
   * ���Զ�Ӧ�ı�ע
   */
  String getFieldComment();
}