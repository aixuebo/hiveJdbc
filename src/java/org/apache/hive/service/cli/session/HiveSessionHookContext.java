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

import org.apache.hadoop.hive.conf.HiveConf;
/**
 * HiveSessionHookContext.
 * Interface passed to the HiveServer2 session hook execution. This enables
 * the hook implementation to accesss session config, user and session handle
 * 一个session连接的上下文信息
 */
public interface HiveSessionHookContext {

  /**
   * Retrieve session conf
   * @return
   * 可以获取该session的配置信息
   */
  public HiveConf getSessionConf();

  /**
   * The get the username starting the session
   * @return
   * 获取该session的登录用户
   */
  public String getSessionUser();

  /**
   * Retrieve handle for the session
   * @return
   * 获取该session的SessionHandle对象,即该session的公钥信息
   */
  public String getSessionHandle();
}
