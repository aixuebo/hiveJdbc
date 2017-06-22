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

import java.util.Map;
/*
 * The equality is implemented fully, the greater-than/less-than
 * values do not implement a transitive relation.
 *
 * 先比较key,必须key都在两个map中存在,否则比较完成
 * 如果key都存在的情况下,比较value大小
 *
 * 该比较方式性能存在问题
 *
 * 该比较方式针对于第一个map的key在第二个map中是否存在为重要指标,此时与FullMapEqualComparer比较方式是不同的
 */
public class CrossMapEqualComparer implements MapEqualComparer {
  @Override
  public int compare(Object o1, MapObjectInspector moi1, Object o2, MapObjectInspector moi2) {
    //先比较两个map的size
    int mapsize1 = moi1.getMapSize(o1);
    int mapsize2 = moi2.getMapSize(o2);
    if (mapsize1 != mapsize2) {
      return mapsize1 - mapsize2;
    }

    //两个map的key类型
    ObjectInspector mkoi1 = moi1.getMapKeyObjectInspector();
    ObjectInspector mkoi2 = moi2.getMapKeyObjectInspector();

    //两个map的value类型
    ObjectInspector mvoi1 = moi1.getMapValueObjectInspector();
    ObjectInspector mvoi2 = moi2.getMapValueObjectInspector();

    //获取两个map的内容
    Map<?, ?> map1 = moi1.getMap(o1);
    Map<?, ?> map2 = moi2.getMap(o2);
    for (Object mk1 : map1.keySet()) {//循环map1中所有key
      boolean notFound = true;//默认是true,表示没有发现两个map的key是相同的
      for (Object mk2 : map2.keySet()) {//循环map2中所有key
        int rc = ObjectInspectorUtils.compare(mk1, mkoi1, mk2, mkoi2, this);//先拿出key的值来进行比较
        if (rc != 0) {//key不同则continue,直到找到key相同的为止
          continue;
        }
        notFound = false;//说明发现key相同的了,因此将没有发现变量设置为false
        Object mv1 = map1.get(mk1);
        Object mv2 = map2.get(mk2);
        rc = ObjectInspectorUtils.compare(mv1, mvoi1, mv2, mvoi2, this);//拿出value的值来进行比较
        if (rc != 0) {//返回key相同时,value不同的结果
          return rc;
        } else {
          break;//找到key相同的后,就不再查找了,继续查找下一个key
        }
      }
      if (notFound) {//说明key不存在,则说明比较成功
        return 1;
      }
    }
    return 0;
  }

}
