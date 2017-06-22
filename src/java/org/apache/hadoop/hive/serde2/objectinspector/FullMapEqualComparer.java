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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/*
 * The equality is implemented fully, the implementation sorts the maps
 * by their keys to provide a transitive compare.
 *
 * 先比较key,
 * 如果key都相同的情况下,比较value大小
 *
 * 该比较方式性能比CrossMapEqualComparer好
 *
 */
public class FullMapEqualComparer implements MapEqualComparer {

  //如何对key的值进行排序
  private static class MapKeyComparator implements Comparator<Object> {

    private ObjectInspector oi;

    MapKeyComparator(ObjectInspector oi) {
      this.oi = oi;
    }

    @Override
    public int compare(Object o1, Object o2) {
      return ObjectInspectorUtils.compare(o1, oi, o2, oi);
    }
  }

    /**
     * 比较两个map对象的大小
     * @param o1 map1对象的具体实例内容
     * @param moi1 map1对象存储的key和value类型的描述
     * @param o2 map2对象的具体实例内容
     * @param moi2 map2对象存储的key和value类型的描述
     */
  @Override
  public int compare(Object o1, MapObjectInspector moi1, Object o2, MapObjectInspector moi2) {
    //首先size大小必须相同,不同则可以比较成功
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

    //两个map的具体的值
    Map<?, ?> map1 = moi1.getMap(o1);
    Map<?, ?> map2 = moi2.getMap(o2);

    //对两个map的key的值进行排序
    Object[] sortedMapKeys1 = map1.keySet().toArray();
    Arrays.sort(sortedMapKeys1, new MapKeyComparator(mkoi1));

    Object[] sortedMapKeys2 = map2.keySet().toArray();
    Arrays.sort(sortedMapKeys2, new MapKeyComparator(mkoi2));

    for (int i = 0; i < mapsize1; ++i) {
      Object mk1 = sortedMapKeys1[i];
      Object mk2 = sortedMapKeys2[i];
      int rc = ObjectInspectorUtils.compare(mk1, mkoi1, mk2, mkoi2, this);//对key的值进行比较
      if (rc != 0) {//不相同则返回比较结果
        return rc;
      }

      //此时说明key相同,因此对value进行比较
      Object mv1 = map1.get(mk1);
      Object mv2 = map2.get(mk2);
      rc = ObjectInspectorUtils.compare(mv1, mvoi1, mv2, mvoi2, this);
      if (rc != 0) {
        return rc;
      }
    }
    return 0;
  }

}
