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

package org.apache.hadoop.hive.ql.exec;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.plan.ExprNodeDesc;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

/**
 * ExprNodeEvaluator.
 *
 */
public abstract class ExprNodeEvaluator<T extends ExprNodeDesc> {

  protected final T expr;//输入的表达式
  protected ObjectInspector outputOI;//表达式的返回值hive类型

  public ExprNodeEvaluator(T expr) {
    this.expr = expr;
  }

  /**
   * Return child evaluators if exist
   */
  public T getExpr() {
    return expr;
  }

  /**
   * Initialize should be called once and only once. Return the ObjectInspector
   * for the return value, given the rowInspector.
   * 初始化表达式的类型
   * 参数是这个表的列的表达式集合
   */
  public abstract ObjectInspector initialize(ObjectInspector rowInspector) throws HiveException;

  /**
   * Return initialized ObjectInspector. If it's not initilized, throws runtime exception
   */
  public ObjectInspector getOutputOI() {
    if (outputOI == null) {
      throw new IllegalStateException("Evaluator is not initialized");
    }
    return outputOI;
  }

  private transient int version = -1;
  private transient Object evaluation;

  public Object evaluate(Object row) throws HiveException {
    return evaluate(row, -1);
  }

  /**
   * Evaluate the expression given the row. This method should use the
   * rowInspector passed in from initialize to inspect the row object. The
   * return value will be inspected by the return value of initialize.
   * If this evaluator is referenced by others, store it for them
   */
  protected Object evaluate(Object row, int version) throws HiveException {
    if (version < 0 || version != this.version) {
      this.version = version;
      return evaluation = _evaluate(row, version);
    }
    return evaluation;
  }

  /**
   * Evaluate value
   * 执行表达式,返回表达式具体的值
   */
  protected abstract Object _evaluate(Object row, int version) throws HiveException;

  /**
   * Return whether this node (and all children nodes) are deterministic.
   */
  public boolean isDeterministic() {
    return true;
  }
  
  /**
   * Return whether this node (or any children nodes) are stateful.
   */
  public boolean isStateful() {
    return false;
  }

  /**
   * Return child evaluators if exist
   */
  public ExprNodeEvaluator[] getChildren() {
    return null;
  }
}
