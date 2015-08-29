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

package org.apache.hadoop.hive.ql.plan;

import java.io.Serializable;

/**
 * CreateFunctionDesc.
 * 创建自定义函数
 * CREATE TEMPORARY FUNCTION xxx as xxx创建函数功能
 */
@Explain(displayName = "Create Function")
public class CreateFunctionDesc implements Serializable {
  private static final long serialVersionUID = 1L;

  private String functionName;//函数名称
  private String className;//函数对应的class全路径

  /**
   * For serialization only.
   */
  public CreateFunctionDesc() {
  }
  
  public CreateFunctionDesc(String functionName, String className) {
    this.functionName = functionName;
    this.className = className;
  }

  @Explain(displayName = "name")
  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  @Explain(displayName = "class")
  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

}
