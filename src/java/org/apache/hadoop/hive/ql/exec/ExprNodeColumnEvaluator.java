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
 * 获取一行数据中的某一个列的值---该值可以是不断用.推导下去的
 */
public class ExprNodeColumnEvaluator extends ExprNodeEvaluator<ExprNodeColumnDesc> {

  transient boolean simpleCase;
  transient StructObjectInspector inspector;//该列所在表的列对象schema集合
  transient StructField field;//该列的类型

  //用于复杂对象
  transient StructObjectInspector[] inspectors;//每一个属性所属的一行数据的schema
  transient StructField[] fields;//每一个属性对应的类型
  transient boolean[] unionField;//true表示该属性是union类型的

  public ExprNodeColumnEvaluator(ExprNodeColumnDesc expr) {
    super(expr);
  }

    /**
     复杂对象
     地址:省份、城市以及住址，住址又包接到门牌号等
     current_address <province:String,city:String,street_address:<street_number:int,street_name:string,street_type:string> >
     SELECT
     current_address.street_address.street_number,
     current_address.street_address.street_name,
     current_address.street_address.street_type,
     current_address.province,
     current_address.city
     FROM struct_demo;
     比如current_address.street_address.street_number,
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
    if (names.length == 1 && unionfields.length == 1) {//说明是简单字段
      simpleCase = true;
      inspector = (StructObjectInspector) rowInspector;
      field = inspector.getStructFieldRef(names[0]);//获取该列的类型
      return outputOI = field.getFieldObjectInspector();//直接初始化表达式的返回值
    }

    //说明表达式字段不是一个简单的对象,比如是struct的一个属性
    //比如current_address.street_address.street_number
    simpleCase = false;
    inspectors = new StructObjectInspector[names.length];//记录属性所属的对象
    fields = new StructField[names.length];//获取每一个属性对应的类型
    unionField = new boolean[names.length];//如果该属性使用下标了,则设置true
    int unionIndex = -1;

    for (int i = 0; i < names.length; i++) {
      if (i == 0) {
        inspectors[0] = (StructObjectInspector) rowInspector;//第一个对象想要获取current_address属性对应的值,因此存储该属性的schema就是行本身
      } else {
        if (unionIndex != -1) {//如果设置了下标
          inspectors[i] = (StructObjectInspector) (
              (UnionObjectInspector)fields[i-1].getFieldObjectInspector()).//先获取上一个union对象
              getObjectInspectors().get(unionIndex);//获取该union对象的第几个对象
        } else {
          inspectors[i] = (StructObjectInspector) fields[i - 1]
              .getFieldObjectInspector();//获取上一个属性对应的schema
        }
      }
      // to support names like _colx:1._coly
      unionfields = names[i].split("\\:");//:应该用于UnionObjectInspector,暂时先介绍没有的情况
      fields[i] = inspectors[i].getStructFieldRef(unionfields[0]);//获取current_address属性对应的struct的schema类型
      if (unionfields.length > 1) {//没有下标
        unionIndex = Integer.parseInt(unionfields[1]);
        unionField[i] = true;
      } else {
        unionIndex = -1;
        unionField[i] = false;
      }
    }
    return outputOI = fields[names.length - 1].getFieldObjectInspector();//获取最后一个对象的返回值
  }

  @Override
  protected Object _evaluate(Object row, int version) throws HiveException {
    if (simpleCase) {//说明该字段是一个简单字段
      return inspector.getStructFieldData(row, field);//直接返回该字段对应的值
    }
    //说明该字段是一个复杂对象
    Object o = row;
    for (int i = 0; i < fields.length; i++) {//循环每一个属性
      o = inspectors[i].getStructFieldData(o, fields[i]);//获取第一个属性,他也是一个struct或者union
      if (unionField[i]) {
        o = ((StandardUnion)o).getObject();//如果是union则从union中获取一个类型
      }
    }
    return o;
  }

}
