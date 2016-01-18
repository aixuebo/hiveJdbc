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

import java.util.ArrayList;

/**
 * 
 * This class stores all the information specified in the TABLESAMPLE clause.
 * e.g. for the clause "FROM t TABLESAMPLE(1 OUT OF 2 ON c1) it will store the
 * numerator 1, the denominator 2 and the list of expressions(in this case c1)
 * in the appropriate fields. The afore-mentioned sampling clause causes the 1st
 * bucket to be picked out of the 2 buckets created by hashing on c1.
 * 
 *    * //[dbName.] tableName [(key=value,key=value,key)] [tableSample] [ as Identifier ]
TABLESAMPLE(BUCKET 数字    OUT OF 数字  [ ON expression,expression ] )
    该类是from子句用于抽样提取,该类代表解析以下字符:TABLESAMPLE(BUCKET 数字    OUT OF 数字  [ ON expression,expression ] )
    
 */
public class TableSample {

  /**
   * The numerator of the TABLESAMPLE clause.
   * 第一个参数
   * 解析TABLESAMPLE(BUCKET 数字    OUT OF 数字  [ ON expression,expression ] )的第一个数字参数
   */
  private int numerator;

  /**
   * The denominator of the TABLESAMPLE clause.
   * 第二个参数
   * 解析TABLESAMPLE(BUCKET 数字    OUT OF 数字  [ ON expression,expression ] )的第二个数字参数
   */
  private int denominator;

  /**
   * The list of expressions following ON part of the TABLESAMPLE clause. This
   * list is empty in case there are no expressions such as in the clause
   * "FROM t TABLESAMPLE(1 OUT OF 2)". For this expression the sampling is done
   * on the tables clustering column(as specified when the table was created).
   * In case the table does not have any clustering column, the usage of a table
   * sample clause without an ON part is disallowed by the compiler
   * on后面的表达式集合,仅仅用于TABLESAMPLE(BUCKET 数字    OUT OF 数字  [ ON expression,expression ] )子句
   */
  private ArrayList<ASTNode> exprs;

  /**
   * Flag to indicate that input files can be pruned.
   * true表示输入的文件可以被修剪,即抽样的时候TABLESAMPLE(BUCKET 数字    OUT OF 数字  [ ON expression,expression ] )子句 表示会进行修剪输入源
   */
  private boolean inputPruning;

  /**
   * Constructs the TableSample given the numerator, denominator and the list of
   * ON clause expressions.
   * 
   * @param num
   *          The numerator
   * @param den
   *          The denominator
   * @param exprs
   *          The list of expressions in the ON part of the TABLESAMPLE clause
   * 用于解析TABLESAMPLE(BUCKET 数字    OUT OF 数字  [ ON expression,expression ] )
   */
  public TableSample(String num, String den, ArrayList<ASTNode> exprs) {
    numerator = Integer.valueOf(num).intValue();
    denominator = Integer.valueOf(den).intValue();
    this.exprs = exprs;
  }

  public TableSample(int num, int den) {
    numerator = num;
    denominator = den;
    exprs = null;
  }

  /**
   * Gets the numerator.
   * 
   * @return int
   */
  public int getNumerator() {
    return numerator;
  }

  /**
   * Sets the numerator.
   * 
   * @param num
   *          The numerator
   */
  public void setNumerator(int num) {
    numerator = num;
  }

  /**
   * Gets the denominator.
   * 
   * @return int
   */
  public int getDenominator() {
    return denominator;
  }

  /**
   * Sets the denominator.
   * 
   * @param den
   *          The denominator
   */
  public void setDenominator(int den) {
    denominator = den;
  }

  /**
   * Gets the ON part's expression list.
   * 
   * @return ArrayList<ASTNode>
   */
  public ArrayList<ASTNode> getExprs() {
    return exprs;
  }

  /**
   * Sets the expression list.
   * 
   * @param exprs
   *          The expression list
   */
  public void setExprs(ArrayList<ASTNode> exprs) {
    this.exprs = exprs;
  }

  /**
   * Gets the flag that indicates whether input pruning is possible.
   * 
   * @return boolean
   */
  public boolean getInputPruning() {
    return inputPruning;
  }

  /**
   * Sets the flag that indicates whether input pruning is possible or not.
   * 
   * @param inputPruning
   *          true if input pruning is possible
   */
  public void setInputPruning(boolean inputPruning) {
    this.inputPruning = inputPruning;
  }
}
