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

package org.apache.hadoop.hive.serde2;

import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.io.Writable;

/**
 * HiveSerializer is used to serialize data to a Hadoop Writable object. The
 * serialize In addition to the interface below, all implementations are assume
 * to have a ctor that takes a single 'Table' object as argument.
 * All serializers should extend the abstract class AbstractSerializer, and eventually
 * Serializer interface should be removed
 * 序列化
 */
@Deprecated
public interface Serializer {

  /**
   * Initialize the HiveSerializer.
   *
   * @param conf
   *          System properties
   * @param tbl
   *          table properties
   * @throws SerDeException
   */
  void initialize(Configuration conf, Properties tbl) throws SerDeException;

  /**
   * Returns the Writable class that would be returned by the serialize method.
   * This is used to initialize SequenceFile header.
   * 序列化的对象
   */
  Class<? extends Writable> getSerializedClass();

  /**
   * Serialize an object by navigating inside the Object with the
   * ObjectInspector. In most cases, the return value of this function will be
   * constant since the function will reuse the Writable object. If the client
   * wants to keep a copy of the Writable, the client needs to clone the
   * returned value.
   * 将一个属性值进行序列化,序列化成hadoop的结果
   * obj是具体的值,objInspector是obj的列对应的类型
   */
  Writable serialize(Object obj, ObjectInspector objInspector) throws SerDeException;

  /**
   * Returns statistics collected when serializing
   * 返回序列化的统计信息,当前仅仅支持数据行数统计信息
   */
  SerDeStats getSerDeStats();
}
