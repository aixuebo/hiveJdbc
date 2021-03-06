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

package org.apache.hive.service.cli;

import java.util.UUID;

import org.apache.hive.service.cli.thrift.TSessionHandle;


/**
 * SessionHandle.
 * session持有一个公钥和私钥对象
 */
public class SessionHandle extends Handle {

    //创建一个公钥和私钥
  public SessionHandle() {
    super();
  }

  //参数传递过来一个公钥和私钥
  public SessionHandle(HandleIdentifier handleId) {
    super(handleId);
  }

    //参数传递过来一个公钥和私钥
  public SessionHandle(TSessionHandle tSessionHandle) {
    super(tSessionHandle.getSessionId());
  }

  //返回公钥
  public UUID getSessionId() {
    return getHandleIdentifier().getPublicId();
  }

    //thrift形式表示一个session持有的公钥和私钥
  public TSessionHandle toTSessionHandle() {
    TSessionHandle tSessionHandle = new TSessionHandle();
    tSessionHandle.setSessionId(getHandleIdentifier().toTHandleIdentifier());
    return tSessionHandle;
  }

  @Override
  public String toString() {
    return "SessionHandle [" + getHandleIdentifier() + "]";
  }
}
