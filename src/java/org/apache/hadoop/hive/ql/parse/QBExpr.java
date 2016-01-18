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

package org.apache.hadoop.hive.ql.parse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the query block expression.
 * 查询语句之间的关系
 * 
 * QBExpr对象表示一个子查询对象
 * 一个别名对应一个子查询对象,
 * 
 * 解析from (select...) alias
 * 为子查询创建一个表达式对象,因为该表达式也包含一组QB对象
 **/
public class QBExpr {

  private static final Log LOG = LogFactory.getLog("hive.ql.parse.QBExpr");

  /**
   * Opcode.
   *
   */
  public static enum Opcode {
    NULLOP,//表示子查询就是正常的一个sql 
    UNION,//表示子查询中使用了union语法产生的子查询 
    INTERSECT, DIFF
  };

  private Opcode opcode;//子查询是一个什么逻辑
  //只有当子查询是UNION时候使用,即UNION表示两个子查询做关联
  private QBExpr qbexpr1;//当为UNION的时候,需要两个查询语句,因此分别是qbexpr1和qbexpr2
  private QBExpr qbexpr2;//当为UNION的时候,需要两个查询语句,因此分别是qbexpr1和qbexpr2
  private QB qb;//解析对应的子查询内的查询sql对象
  private String alias;//该子查询的别名,即解析from (select...) alias中的alias

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public QBExpr(String alias) {
    this.alias = alias;
  }

  public QBExpr(QB qb) {
    opcode = Opcode.NULLOP;
    this.qb = qb;
  }

  public QBExpr(Opcode opcode, QBExpr qbexpr1, QBExpr qbexpr2) {
    this.opcode = opcode;
    this.qbexpr1 = qbexpr1;
    this.qbexpr2 = qbexpr2;
  }

  public void setQB(QB qb) {
    this.qb = qb;
  }

  public void setOpcode(Opcode opcode) {
    this.opcode = opcode;
  }

  public void setQBExpr1(QBExpr qbexpr) {
    qbexpr1 = qbexpr;
  }

  public void setQBExpr2(QBExpr qbexpr) {
    qbexpr2 = qbexpr;
  }

  public QB getQB() {
    return qb;
  }

  public Opcode getOpcode() {
    return opcode;
  }

  public QBExpr getQBExpr1() {
    return qbexpr1;
  }

  public QBExpr getQBExpr2() {
    return qbexpr2;
  }

  public void print(String msg) {
    if (opcode == Opcode.NULLOP) {
      LOG.info(msg + "start qb = " + qb);
      qb.print(msg + " ");
      LOG.info(msg + "end qb = " + qb);
    } else {
      LOG.info(msg + "start qbexpr1 = " + qbexpr1);
      qbexpr1.print(msg + " ");
      LOG.info(msg + "end qbexpr1 = " + qbexpr1);
      LOG.info(msg + "start qbexpr2 = " + qbexpr2);
      qbexpr2.print(msg + " ");
      LOG.info(msg + "end qbexpr2 = " + qbexpr2);
    }
  }

}
