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

package org.apache.hadoop.hive.ql;

/**
 *
 * QueryProperties.
 *
 * A structure to contain features of a query that are determined
 * during parsing and may be useful for categorizing a query type
 *
 * These inlucde whether the query contains:
 * a join clause, a group by clause, an order by clause, a sort by
 * clause, a group by clause following a join clause, and whether
 * the query uses a script for mapping/reducing
 */
public class QueryProperties {

  boolean hasJoin = false;//from子句是否有join子句
  boolean hasGroupBy = false;//是否设置了group by
  boolean hasOrderBy = false;//是否设置了order by
  boolean hasSortBy = false;//是否设置了sort by,是分桶ClusterBy的一个子步骤,但是仅仅排序,但是没有按照什么字段去进行分桶操作
  boolean hasJoinFollowedByGroupBy = false;//是否在有join表链接的情况下,依然设置了group by语句
  boolean hasPTF = false;//from子句使用了partitionTableFunctionSource,属于窗口函数的应用
  boolean hasWindowing = false;//使用了窗口函数

  // does the query have a using clause
  boolean usesScript = false;

  boolean hasDistributeBy = false;//是否设置了Distribute by,仅仅是类似分桶,将按照哪些字段进行分桶,但是不排序,但是没有sort排序功能
  boolean hasClusterBy = false;//是否设置了Cluster by,即设置了分桶,按照哪个字段分到多少个桶里面,桶里面按照什么顺序排序,都可以设置,多少个分桶,就相当于多少个reduce文件
  boolean mapJoinRemoved = false;//true表示忽略map-join的hint
  boolean hasMapGroupBy = false;

  public boolean hasJoin() {
    return hasJoin;
  }

  public void setHasJoin(boolean hasJoin) {
    this.hasJoin = hasJoin;
  }

  public boolean hasGroupBy() {
    return hasGroupBy;
  }

  public void setHasGroupBy(boolean hasGroupBy) {
    this.hasGroupBy = hasGroupBy;
  }

  public boolean hasOrderBy() {
    return hasOrderBy;
  }

  public void setHasOrderBy(boolean hasOrderBy) {
    this.hasOrderBy = hasOrderBy;
  }

  public boolean hasSortBy() {
    return hasSortBy;
  }

  public void setHasSortBy(boolean hasSortBy) {
    this.hasSortBy = hasSortBy;
  }

  public boolean hasJoinFollowedByGroupBy() {
    return hasJoinFollowedByGroupBy;
  }

  public void setHasJoinFollowedByGroupBy(boolean hasJoinFollowedByGroupBy) {
    this.hasJoinFollowedByGroupBy = hasJoinFollowedByGroupBy;
  }

  public boolean usesScript() {
    return usesScript;
  }

  public void setUsesScript(boolean usesScript) {
    this.usesScript = usesScript;
  }

  public boolean hasDistributeBy() {
    return hasDistributeBy;
  }

  public void setHasDistributeBy(boolean hasDistributeBy) {
    this.hasDistributeBy = hasDistributeBy;
  }

  public boolean hasClusterBy() {
    return hasClusterBy;
  }

  public void setHasClusterBy(boolean hasClusterBy) {
    this.hasClusterBy = hasClusterBy;
  }

  public boolean hasPTF() {
    return hasPTF;
  }

  public void setHasPTF(boolean hasPTF) {
    this.hasPTF = hasPTF;
  }

  public boolean hasWindowing() {
    return hasWindowing;
  }

  public void setHasWindowing(boolean hasWindowing) {
    this.hasWindowing = hasWindowing;
  }

  public boolean isMapJoinRemoved() {
    return mapJoinRemoved;
  }

  public void setMapJoinRemoved(boolean mapJoinRemoved) {
    this.mapJoinRemoved = mapJoinRemoved;
  }

  public boolean isHasMapGroupBy() {
    return hasMapGroupBy;
  }

  public void setHasMapGroupBy(boolean hasMapGroupBy) {
    this.hasMapGroupBy = hasMapGroupBy;
  }
}
