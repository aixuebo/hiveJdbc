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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.conf.HiveConf;

/**
 * AbstractService.
 *
 */
public abstract class AbstractService implements Service {

  private static final Log LOG = LogFactory.getLog(AbstractService.class);

  /**
   * Service state: initially {@link STATE#NOTINITED}.
   * 初始服务状态
   */
  private STATE state = STATE.NOTINITED;

  /**
   * Service name.
   */
  private final String name;
  /**
   * Service start time. Will be zero until the service is started.
   * 服务开始时间
   */
  private long startTime;

  /**
   * The configuration. Will be null until the service is initialized.
   */
  private HiveConf hiveConf;

  /**
   * List of state change listeners; it is final to ensure
   * that it will never be null.
   * 该服务的监听,即该服务状态变化的时候要通知这些监听
   */
  private final List<ServiceStateChangeListener> listeners =
      new ArrayList<ServiceStateChangeListener>();

  /**
   * Construct the service.
   *
   * @param name
   *          service name
   */
  public AbstractService(String name) {
    this.name = name;
  }

  @Override
  public synchronized STATE getServiceState() {
    return state;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException
   *           if the current service state does not permit
   *           this action
   */
  @Override
  public synchronized void init(HiveConf hiveConf) {
    ensureCurrentState(STATE.NOTINITED);
    this.hiveConf = hiveConf;
    changeState(STATE.INITED);
    LOG.info("Service:" + getName() + " is inited.");
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException
   *           if the current service state does not permit
   *           this action
   */
  @Override
  public synchronized void start() {
    startTime = System.currentTimeMillis();
    ensureCurrentState(STATE.INITED);
    changeState(STATE.STARTED);
    LOG.info("Service:" + getName() + " is started.");
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException
   *           if the current service state does not permit
   *           this action
   */
  @Override
  public synchronized void stop() {
    if (state == STATE.STOPPED ||
        state == STATE.INITED ||
        state == STATE.NOTINITED) {
      // already stopped, or else it was never
      // started (eg another service failing canceled startup)
      return;
    }
    ensureCurrentState(STATE.STARTED);
    changeState(STATE.STOPPED);
    LOG.info("Service:" + getName() + " is stopped.");
  }

  @Override
  public synchronized void register(ServiceStateChangeListener l) {
    listeners.add(l);
  }

  @Override
  public synchronized void unregister(ServiceStateChangeListener l) {
    listeners.remove(l);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public synchronized HiveConf getHiveConf() {
    return hiveConf;
  }

  @Override
  public long getStartTime() {
    return startTime;
  }

  /**
   * Verify that that a service is in a given state.
   *
   * @param currentState
   *          the desired state
   * @throws IllegalStateException
   *           if the service state is different from
   *           the desired state
   * 确保参数的当前状态确实是当前状态
   */
  private void ensureCurrentState(STATE currentState) {
    ServiceOperations.ensureCurrentState(state, currentState);
  }

  /**
   * Change to a new state and notify all listeners.
   * This is a private method that is only invoked from synchronized methods,
   * which avoid having to clone the listener list. It does imply that
   * the state change listener methods should be short lived, as they
   * will delay the state transition.
   *
   * @param newState
   *          new service state
   * 去更改服务的状态
   */
  private void changeState(STATE newState) {
    state = newState;
    // notify listeners
    for (ServiceStateChangeListener l : listeners) {//通知监听器服务状态有更改
      l.stateChanged(this);
    }
  }

}
