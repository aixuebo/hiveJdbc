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

package org.apache.hadoop.hive.ql.hooks;

import java.util.Set;

import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * The post execute hook interface. A list of such hooks can be configured to be
 * called after compilation and before execution.
 */
public interface PostExecute extends Hook {

  /**
   * The run command that is called just before the execution of the query.
   * 查询命令后执行该接口,进行后续处理,没有返回值,仅仅是处理,但是有异常可以抛出,停止查询流程
   * @param sess
   *          The session state.
   * @param inputs
   *          The set of input tables and partitions.
   * @param outputs
   *          The set of output tables, partitions, local and hdfs directories.
   * @param lInfo
   *           The column level lineage information.
   * @param ugi
   *          The user group security information.
   */
  @Deprecated
  void run(SessionState sess, Set<ReadEntity> inputs,
      Set<WriteEntity> outputs, LineageInfo lInfo,
      UserGroupInformation ugi) throws Exception;

}
