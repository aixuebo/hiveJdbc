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
 * �ȱȽ�key,����key��������map�д���,����Ƚ����
 * ���key�����ڵ������,�Ƚ�value��С
 *
 * �ñȽϷ�ʽ���ܴ�������
 *
 * �ñȽϷ�ʽ����ڵ�һ��map��key�ڵڶ���map���Ƿ����Ϊ��Ҫָ��,��ʱ��FullMapEqualComparer�ȽϷ�ʽ�ǲ�ͬ��
 */
public class CrossMapEqualComparer implements MapEqualComparer {
  @Override
  public int compare(Object o1, MapObjectInspector moi1, Object o2, MapObjectInspector moi2) {
    //�ȱȽ�����map��size
    int mapsize1 = moi1.getMapSize(o1);
    int mapsize2 = moi2.getMapSize(o2);
    if (mapsize1 != mapsize2) {
      return mapsize1 - mapsize2;
    }

    //����map��key����
    ObjectInspector mkoi1 = moi1.getMapKeyObjectInspector();
    ObjectInspector mkoi2 = moi2.getMapKeyObjectInspector();

    //����map��value����
    ObjectInspector mvoi1 = moi1.getMapValueObjectInspector();
    ObjectInspector mvoi2 = moi2.getMapValueObjectInspector();

    //��ȡ����map������
    Map<?, ?> map1 = moi1.getMap(o1);
    Map<?, ?> map2 = moi2.getMap(o2);
    for (Object mk1 : map1.keySet()) {//ѭ��map1������key
      boolean notFound = true;//Ĭ����true,��ʾû�з�������map��key����ͬ��
      for (Object mk2 : map2.keySet()) {//ѭ��map2������key
        int rc = ObjectInspectorUtils.compare(mk1, mkoi1, mk2, mkoi2, this);//���ó�key��ֵ�����бȽ�
        if (rc != 0) {//key��ͬ��continue,ֱ���ҵ�key��ͬ��Ϊֹ
          continue;
        }
        notFound = false;//˵������key��ͬ����,��˽�û�з��ֱ�������Ϊfalse
        Object mv1 = map1.get(mk1);
        Object mv2 = map2.get(mk2);
        rc = ObjectInspectorUtils.compare(mv1, mvoi1, mv2, mvoi2, this);//�ó�value��ֵ�����бȽ�
        if (rc != 0) {//����key��ͬʱ,value��ͬ�Ľ��
          return rc;
        } else {
          break;//�ҵ�key��ͬ�ĺ�,�Ͳ��ٲ�����,����������һ��key
        }
      }
      if (notFound) {//˵��key������,��˵���Ƚϳɹ�
        return 1;
      }
    }
    return 0;
  }

}
