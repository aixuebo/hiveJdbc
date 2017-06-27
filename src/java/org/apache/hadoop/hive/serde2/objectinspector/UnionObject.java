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
 * The UnionObject.
 *
 * It has tag followed by the object it is holding.
 * 代表一个union中的一个元素
 */
public interface UnionObject {
  /**
   * Get the tag of the union.
   * 表示union中的下标序号
   * @return the tag byte
   */
  byte getTag();

  /**
   * Get the Object.
   * 表示union中该序号对应的值
   * @return The Object union is holding
   */
  Object getObject();

}
