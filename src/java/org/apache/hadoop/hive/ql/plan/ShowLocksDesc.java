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
import java.util.HashMap;

import org.apache.hadoop.fs.Path;

/**
 * ShowLocksDesc.
 * SHOW LOCKS xxx .($ELEM$ | $KEY$ | $VALUE$ | xxx ) .($ELEM$ | $KEY$ | $VALUE$ | xxx )
 */
@Explain(displayName = "Show Locks")
public class ShowLocksDesc extends DDLDesc implements Serializable {
  private static final long serialVersionUID = 1L;
  String resFile;
  String tableName;
  HashMap<String, String> partSpec;
  boolean isExt;

  /**
   * table name for the result of show locks.
   */
  private static final String table = "showlocks";
  /**
   * thrift ddl for the result of show locks.
   */
  private static final String schema = "tab_name,mode#string:string";

  public String getTable() {
    return table;
  }

  public String getSchema() {
    return schema;
  }

  public ShowLocksDesc() {
  }

  /**
   * @param resFile
   */
  public ShowLocksDesc(Path resFile, String tableName,
                       HashMap<String, String> partSpec, boolean isExt) {
    this.resFile   = resFile.toString();
    this.partSpec  = partSpec;
    this.tableName = tableName;
    this.isExt     = isExt;
  }

  /**
   * @return the tableName
   */
  @Explain(displayName = "table")
  public String getTableName() {
    return tableName;
  }

  /**
   * @param tableName
   *          the tableName to set
   */
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   * @return the partSpec
   */
  @Explain(displayName = "partition")
  public HashMap<String, String> getPartSpec() {
    return partSpec;
  }

  /**
   * @param partSpec
   *          the partSpec to set
   */
  public void setPartSpecs(HashMap<String, String> partSpec) {
    this.partSpec = partSpec;
  }

  /**
   * @return the resFile
   */
  @Explain(displayName = "result file", normalExplain = false)
  public String getResFile() {
    return resFile;
  }

  /**
   * @param resFile
   *          the resFile to set
   */
  public void setResFile(String resFile) {
    this.resFile = resFile;
  }

  /**
   * @return the isExt
   */
  public boolean isExt() {
    return isExt;
  }

  /**
   * @param isExt
   *          the isExt to set
   */
  public void setExt(boolean isExt) {
    this.isExt = isExt;
  }
}
