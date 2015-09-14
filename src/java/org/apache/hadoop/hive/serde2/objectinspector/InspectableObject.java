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

package org.apache.hadoop.hive.serde2.objectinspector;

/**
 * Simple wrapper of object with ObjectInspector.
 * 简单的对对象检查器进行包装
 * TODO: we need to redefine the hashCode and equals methods, so that it can be
 * put into a HashMap as a key.
 * 
 * This class also serves as a facility for a function that returns both an
 * object and an ObjectInspector.
 */
public class InspectableObject {

  public Object o;
  public ObjectInspector oi;//被包装的 对象检查器

  public InspectableObject() {
    this(null, null);
  }

  public InspectableObject(Object o, ObjectInspector oi) {
    this.o = o;
    this.oi = oi;
  }

}
