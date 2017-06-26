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

package org.apache.hive.service;

import org.apache.hadoop.hive.conf.HiveConf;

/**
 * Service.
 * 定义服务接口
 */
public interface Service {

  /**
   * Service states
   * 服务状态
   */
  public enum STATE {
    /** Constructed but not initialized 构造,但是尚未初始化*/
    NOTINITED,

    /** Initialized but not started or stopped 初始化完成了,但是还没有开始和stop*/
    INITED,

    /** started and not stopped 已经开始了*/
    STARTED,

    /** stopped. No further state transitions are permitted 已经停止了*/
    STOPPED
  }

  /**
   * Initialize the service.
   *
   * The transition must be from {@link STATE#NOTINITED} to {@link STATE#INITED} unless the
   * operation failed and an exception was raised.
   *
   * @param config
   *          the configuration of the service
   * 初始化一个服务
   */
  void init(HiveConf conf);


  /**
   * Start the service.
   *
   * The transition should be from {@link STATE#INITED} to {@link STATE#STARTED} unless the
   * operation failed and an exception was raised.
   */
  void start();

  /**
   * Stop the service.
   *
   * This operation must be designed to complete regardless of the initial state
   * of the service, including the state of all its internal fields.
   */
  void stop();

  /**
   * Register an instance of the service state change events.
   *
   * @param listener
   *          a new listener
   * 注册该服务到监听器中,当服务有变化时候,监听器可以知道,处理各种逻辑
   */
  void register(ServiceStateChangeListener listener);

  /**
   * Unregister a previously instance of the service state change events.
   *
   * @param listener
   *          the listener to unregister.
   */
  void unregister(ServiceStateChangeListener listener);

  /**
   * Get the name of this service.
   *
   * @return the service name
   * 服务名字
   */
  String getName();

  /**
   * Get the configuration of this service.
   * This is normally not a clone and may be manipulated, though there are no
   * guarantees as to what the consequences of such actions may be
   *
   * @return the current configuration, unless a specific implementation chooses
   *         otherwise.
   */
  HiveConf getHiveConf();

  /**
   * Get the current service state
   *
   * @return the state of the service
   * 服务此时状态
   */
  STATE getServiceState();

  /**
   * Get the service start time
   * 返回服务的开始时间
   * @return the start time of the service. This will be zero if the service
   *         has not yet been started.
   */
  long getStartTime();

}
