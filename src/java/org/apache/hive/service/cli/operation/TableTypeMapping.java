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

package org.apache.hive.service.cli.operation;

import java.util.Set;

//hive中标的类型与经典传统的表类型之间的映射关系
//hive表类型为MANAGED_TABLE、EXTERNAL_TABLE、VIRTUAL_VIEW、INDEX_TABLE
//经典表类型为TABLE和VIEW
public interface TableTypeMapping {
  /**
   * Map client's table type name to hive's table type
   * @param clientTypeName
   * @return
   * 经典表如何转变成hive表类型
   */
  public String mapToHiveType (String clientTypeName);

  /**
   * Map hive's table type name to client's table type
   * @param clientTypeName
   * @return
   * hive表类型如何转成经典表类型
   */
  public String mapToClientType (String hiveTypeName);

  /**
   * Get all the table types of this mapping
   * @return
   * 返回所有的表的类型
   */
  public Set<String> getTableTypeNames();
}
