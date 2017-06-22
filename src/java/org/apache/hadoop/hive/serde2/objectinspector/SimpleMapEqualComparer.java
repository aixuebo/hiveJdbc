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
 * Assumes that a getMapValueElement on object2 will work with a key from
 * object1. The equality is implemented fully, the greater-than/less-than
 * values do not implement a transitive relation. 
 */
public class SimpleMapEqualComparer implements MapEqualComparer {

    /**
     * �Ƚ�����map����Ĵ�С
     * @param o1 map1����ľ���ʵ������
     * @param moi1 map1����洢��key��value���͵�����
     * @param o2 map2����ľ���ʵ������
     * @param moi2 map2����洢��key��value���͵�����
     * @return
     */
  @Override
  public int compare(Object o1, MapObjectInspector moi1, Object o2, MapObjectInspector moi2) {
      //����size��С������ͬ,��ͬ����ԱȽϳɹ�
    int mapsize1 = moi1.getMapSize(o1);
    int mapsize2 = moi2.getMapSize(o2);
    if (mapsize1 != mapsize2) {
      return mapsize1 - mapsize2;
    }

    //��ȡ����value������
    ObjectInspector mvoi1 = moi1.getMapValueObjectInspector();
    ObjectInspector mvoi2 = moi2.getMapValueObjectInspector();
    Map<?, ?> map1 = moi1.getMap(o1);//��ȡmap1�ľ���maoֵ
    for (Object mk1: map1.keySet()) {//ѭ��ÿһ��map1��key
      int rc = ObjectInspectorUtils.compare(moi1.getMapValueElement(o1, mk1), mvoi1, 
          moi2.getMapValueElement(o2, mk1), mvoi2, this);//�Ƚ�key��ͬ������map�����Ӧ��ֵ��С������ע���ʱ����map���Ϳ��Բ�һ��,�����Ͳ�һ��Ҳ���Բ���Ƚ�
      if (rc != 0) {
        return rc;
      }
    }
    return 0;
  }
}
