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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PartitionSpec
 * 代表一个数据表对分区表的操作
 */
@Explain(displayName = "Partition specification")
public class PartitionSpec {

  //对分区的描述,比如要删除某些分区的时候,可能是log_day>20151212
  private class PredicateSpec {
    private String operator;//表示>
    private String value;//表示20151212

    public PredicateSpec() {
    }

    public PredicateSpec(String operator, String value) {
      this.operator = operator;
      this.value = value;
    }

    public String getOperator() {
      return this.operator;
    }

    public String getValue() {
      return this.value;
    }

    public void setOperator(String operator) {
      this.operator = operator;
    }

    public void setValue(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      //如果是不等于,则统一为<>输出,即如果输入是!=,也会转换成<>
      return (((this.operator.equals("!="))? "<>": this.operator) + " " + this.value);
    }
  }

  /**
   * 因为一个数据表的分区有若干个属性决定,因此这个是Map形式,例如log_day,hour_day
   * key表示针对什么分区属性进行操作,value表示如何对该分区属性进行过滤
   */
  private final Map<String, PredicateSpec> partSpec;

  public PartitionSpec() {
    this.partSpec = new LinkedHashMap<String, PredicateSpec>();
  }

  /**
   * @param key
   *          partition key name for one partition key compare in the spec
   * @param operator
   *          the operator that is used for the comparison
   * @param value
   *          the value to be compared against
   */
  public void addPredicate(String key, String operator, String value) {
    partSpec.put(key, new PredicateSpec(operator, value));
  }

  /**
   * @param key
   *          partition key to look for in the partition spec
   * @return true if key exists in the partition spec, false otherwise
   */
  public boolean existsKey(String key) {
    return (partSpec.get(key) != null);
  }

  @Override
  public String toString() {
    StringBuilder filterString = new StringBuilder();
    int count = 0;
    for (Map.Entry<String, PredicateSpec> entry: this.partSpec.entrySet()) {
      if (count > 0) {
        filterString.append(" AND ");
      }
      filterString.append(entry.getKey() + " " + entry.getValue().toString());
      count++;
    }
    return filterString.toString();
  }

  // getParitionsByFilter only works for string columns due to a JDO limitation.
  // The operator is only useful if it can be passed as a filter to the metastore.
  // For compatibility with other non-string partition columns, this function
  // returns the key, value mapping assuming that the operator is equality.
  //对所有的value都去除单引号和双引号,其实相当于clone操作
  public Map<String, String> getPartSpecWithoutOperator() {
    Map<String, String> partSpec = new LinkedHashMap<String, String>();
    for (Map.Entry<String, PredicateSpec> entry: this.partSpec.entrySet()) {
      partSpec.put(entry.getKey(), PlanUtils.stripQuotes(entry.getValue().getValue()));
    }
    return partSpec;
  }

  // Again, for the same reason as the above function - getPartSpecWithoutOperator
  //true表示所有的操作中有非=号操作,false表示所有的属性都是=号操作
  public boolean isNonEqualityOperator() {
    Iterator<PredicateSpec> iter = partSpec.values().iterator();
    while (iter.hasNext()) {
      PredicateSpec predSpec = iter.next();
      if (!predSpec.operator.equals("=")) {
        return true;
      }
    }
    return false;
  }
}
