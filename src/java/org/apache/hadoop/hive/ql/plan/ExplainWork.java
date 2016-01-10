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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.hadoop.hive.ql.exec.Task;
import org.apache.hadoop.hive.ql.hooks.ReadEntity;
import org.apache.hadoop.hive.ql.parse.ParseContext;

/**
 * ExplainWork.
 * EXPLAIN [EXTENDED|FORMATTED|DEPENDENCY|LOGICAL] execStatement
 * 参见ExplainSemanticAnalyzer类
 */
public class ExplainWork implements Serializable {
  private static final long serialVersionUID = 1L;

  private String resFile;
  private ArrayList<Task<? extends Serializable>> rootTasks;//sql对应解析后的任务集合
  private String astStringTree;
  private HashSet<ReadEntity> inputs;
  private ParseContext pCtx;

  //解析[EXTENDED|FORMATTED|DEPENDENCY|LOGICAL],属性中只有一次sql只能设置一个值,因此只有一个是true
  boolean extended;
  boolean formatted;
  boolean dependency;
  boolean logical;


  public ExplainWork() {
  }

  public ExplainWork(String resFile,
      ParseContext pCtx,
      List<Task<? extends Serializable>> rootTasks,
      String astStringTree,
      HashSet<ReadEntity> inputs,
      boolean extended,
      boolean formatted,
      boolean dependency,
      boolean logical) {
    this.resFile = resFile;
    this.rootTasks = new ArrayList<Task<? extends Serializable>>(rootTasks);
    this.astStringTree = astStringTree;
    this.inputs = inputs;
    this.extended = extended;
    this.formatted = formatted;
    this.dependency = dependency;
    this.logical = logical;
    this.pCtx = pCtx;
  }

  public String getResFile() {
    return resFile;
  }

  public void setResFile(String resFile) {
    this.resFile = resFile;
  }

  public ArrayList<Task<? extends Serializable>> getRootTasks() {
    return rootTasks;
  }

  public void setRootTasks(ArrayList<Task<? extends Serializable>> rootTasks) {
    this.rootTasks = rootTasks;
  }

  public String getAstStringTree() {
    return astStringTree;
  }

  public void setAstStringTree(String astStringTree) {
    this.astStringTree = astStringTree;
  }

  public HashSet<ReadEntity> getInputs() {
    return inputs;
  }

  public void setInputs(HashSet<ReadEntity> inputs) {
    this.inputs = inputs;
  }

  public boolean getExtended() {
    return extended;
  }

  public void setExtended(boolean extended) {
    this.extended = extended;
  }

  public boolean getDependency() {
    return dependency;
  }

  public void setDependency(boolean dependency) {
    this.dependency = dependency;
  }

  public boolean isFormatted() {
    return formatted;
  }

  public void setFormatted(boolean formatted) {
    this.formatted = formatted;
  }

  public ParseContext getParseContext() {
    return pCtx;
  }

  public void setParseContext(ParseContext pCtx) {
    this.pCtx = pCtx;
  }

  public boolean isLogical() {
    return logical;
  }

  public void setLogical(boolean logical) {
    this.logical = logical;
  }

}
