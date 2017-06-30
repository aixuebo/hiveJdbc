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

package org.apache.hadoop.hive.ql.exec;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.plan.ExprNodeColumnDesc;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.UnionObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StandardUnionObjectInspector.StandardUnion;

/**
 * This evaluator gets the column from the row object.
 * ��ȡһ�������е�ĳһ���е�ֵ---��ֵ�����ǲ�����.�Ƶ���ȥ��
 */
public class ExprNodeColumnEvaluator extends ExprNodeEvaluator<ExprNodeColumnDesc> {

  transient boolean simpleCase;
  transient StructObjectInspector inspector;//�������ڱ���ж���schema����
  transient StructField field;//���е�����

  //���ڸ��Ӷ���
  transient StructObjectInspector[] inspectors;//ÿһ������������һ�����ݵ�schema
  transient StructField[] fields;//ÿһ�����Զ�Ӧ������
  transient boolean[] unionField;//true��ʾ��������union���͵�

  public ExprNodeColumnEvaluator(ExprNodeColumnDesc expr) {
    super(expr);
  }

    /**
     ���Ӷ���
     ��ַ:ʡ�ݡ������Լ�סַ��סַ�ְ��ӵ����ƺŵ�
     current_address <province:String,city:String,street_address:<street_number:int,street_name:string,street_type:string> >
     SELECT
     current_address.street_address.street_number,
     current_address.street_address.street_name,
     current_address.street_address.street_type,
     current_address.province,
     current_address.city
     FROM struct_demo;
     ����current_address.street_address.street_number,
     * @param rowInspector
     * @return
     * @throws HiveException
     */
  @Override
  public ObjectInspector initialize(ObjectInspector rowInspector) throws HiveException {

    // We need to support field names like KEY.0, VALUE.1 between
    // map-reduce boundary.
    String[] names = expr.getColumn().split("\\.");
    String[] unionfields = names[0].split("\\:");
    if (names.length == 1 && unionfields.length == 1) {//˵���Ǽ��ֶ�
      simpleCase = true;
      inspector = (StructObjectInspector) rowInspector;
      field = inspector.getStructFieldRef(names[0]);//��ȡ���е�����
      return outputOI = field.getFieldObjectInspector();//ֱ�ӳ�ʼ�����ʽ�ķ���ֵ
    }

    //˵�����ʽ�ֶβ���һ���򵥵Ķ���,������struct��һ������
    //����current_address.street_address.street_number
    simpleCase = false;
    inspectors = new StructObjectInspector[names.length];//��¼���������Ķ���
    fields = new StructField[names.length];//��ȡÿһ�����Զ�Ӧ������
    unionField = new boolean[names.length];//���������ʹ���±���,������true
    int unionIndex = -1;

    for (int i = 0; i < names.length; i++) {
      if (i == 0) {
        inspectors[0] = (StructObjectInspector) rowInspector;//��һ��������Ҫ��ȡcurrent_address���Զ�Ӧ��ֵ,��˴洢�����Ե�schema�����б���
      } else {
        if (unionIndex != -1) {//����������±�
          inspectors[i] = (StructObjectInspector) (
              (UnionObjectInspector)fields[i-1].getFieldObjectInspector()).//�Ȼ�ȡ��һ��union����
              getObjectInspectors().get(unionIndex);//��ȡ��union����ĵڼ�������
        } else {
          inspectors[i] = (StructObjectInspector) fields[i - 1]
              .getFieldObjectInspector();//��ȡ��һ�����Զ�Ӧ��schema
        }
      }
      // to support names like _colx:1._coly
      unionfields = names[i].split("\\:");//:Ӧ������UnionObjectInspector,��ʱ�Ƚ���û�е����
      fields[i] = inspectors[i].getStructFieldRef(unionfields[0]);//��ȡcurrent_address���Զ�Ӧ��struct��schema����
      if (unionfields.length > 1) {//û���±�
        unionIndex = Integer.parseInt(unionfields[1]);
        unionField[i] = true;
      } else {
        unionIndex = -1;
        unionField[i] = false;
      }
    }
    return outputOI = fields[names.length - 1].getFieldObjectInspector();//��ȡ���һ������ķ���ֵ
  }

  @Override
  protected Object _evaluate(Object row, int version) throws HiveException {
    if (simpleCase) {//˵�����ֶ���һ�����ֶ�
      return inspector.getStructFieldData(row, field);//ֱ�ӷ��ظ��ֶζ�Ӧ��ֵ
    }
    //˵�����ֶ���һ�����Ӷ���
    Object o = row;
    for (int i = 0; i < fields.length; i++) {//ѭ��ÿһ������
      o = inspectors[i].getStructFieldData(o, fields[i]);//��ȡ��һ������,��Ҳ��һ��struct����union
      if (unionField[i]) {
        o = ((StandardUnion)o).getObject();//�����union���union�л�ȡһ������
      }
    }
    return o;
  }

}
