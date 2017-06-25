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

package org.apache.hive.service.cli.session;

import org.apache.hadoop.hive.ql.hooks.Hook;
import org.apache.hive.service.cli.HiveSQLException;

/**
 * HiveSessionHook.
 * HiveServer2 session level Hook interface. The run method is executed
 *  when session manager starts a new session
 *  当一个session被创建的时候,就会执行每一个实现该HiveSessionHookContext接口的所有类,这些类要怎么实现都可以.主要目的是知道session创建了一个新的
 */
public interface HiveSessionHook extends Hook {

  /**
   * @param sessionHookContext context
   * @throws HiveSQLException
   */
  public void run(HiveSessionHookContext sessionHookContext) throws HiveSQLException;
}
