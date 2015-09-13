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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.hive.ql.metadata.Partition;
import org.apache.hadoop.hive.ql.metadata.Table;

/**
 * This class encapsulates the information on the partition and tables that are
 * read by the query.
 * 表示要读取哪张表或者读取哪个partation
 */
public class ReadEntity extends Entity implements Serializable {

  // Consider a query like: select * from V, where the view V is defined as:
  // select * from T
  // The inputs will contain V and T (parent: V)
	
	private static final long serialVersionUID = 1L;
// For views, the entities can be nested - by default, entities are at the top level
	//视图是需要嵌套的,因为查询sql是查询一个视图V,而视图还要查询真正的表T,因此V就是父读实体对象
  private final Set<ReadEntity> parents = new HashSet<ReadEntity>();

  /**
   * For serialization only.
   */
  public ReadEntity() {
    super();
  }

  /**
   * Constructor.
   *
   * @param t
   *          The Table that the query reads from.
   */
  public ReadEntity(Table t) {
    super(t);
  }

  private void initParent(ReadEntity parent) {
    if (parent != null) {
      this.parents.add(parent);
    }
  }

  public ReadEntity(Table t, ReadEntity parent) {
    super(t);
    initParent(parent);
  }

  /**
   * Constructor given a partition.
   *
   * @param p
   *          The partition that the query reads from.
   */
  public ReadEntity(Partition p) {
    super(p);
  }

  public ReadEntity(Partition p, ReadEntity parent) {
    super(p);
    initParent(parent);
  }

  public Set<ReadEntity> getParents() {
    return parents;
  }

  /**
   * Equals function.
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (o instanceof ReadEntity) {
      ReadEntity ore = (ReadEntity) o;
      return (toString().equalsIgnoreCase(ore.toString()));
    } else {
      return false;
    }
  }
}
