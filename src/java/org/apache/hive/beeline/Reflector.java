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

/*
 * This source file is based on code taken from SQLLine 1.0.2
 * See SQLLine notice in LICENSE
 */
package org.apache.hive.beeline;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//反射的方法执行一个实例对象的一个方法
class Reflector {
  private final BeeLine beeLine;

  public Reflector(BeeLine beeLine) {
    this.beeLine = beeLine;
  }

  public Object invoke(Object on, String method, Object[] args)
      throws InvocationTargetException, IllegalAccessException,
      ClassNotFoundException {
    return invoke(on, method, Arrays.asList(args));
  }

  public Object invoke(Object on, String method, List args)
      throws InvocationTargetException, IllegalAccessException,
      ClassNotFoundException {
    return invoke(on, on == null ? null : on.getClass(), method, args);
  }


    /**
     * 反射执行一个方法
     * @param on 实例对象
     * @param defClass 实例对象on是哪个class的实例对象
     * @param method 要执行该对象的哪个方法
     * @param args 方法需要的参数集合
     * @return 返回方法执行的返回值
     */
  public Object invoke(Object on, Class defClass,
      String method, List args)
      throws InvocationTargetException, IllegalAccessException,
      ClassNotFoundException {
    Class c = defClass != null ? defClass : on.getClass();//要执行的实例对象的class
    List<Method> candidateMethods = new LinkedList<Method>();//该实例class中满足参数method方法的集合,因为可能是覆盖很多方法

    //找到所有的method方法集合
    Method[] m = c.getMethods();//实例对象所有的方法
    for (int i = 0; i < m.length; i++) {
      if (m[i].getName().equalsIgnoreCase(method)) {
        candidateMethods.add(m[i]);
      }
    }

    //说明没找到要调用的方法
    if (candidateMethods.size() == 0) {
      throw new IllegalArgumentException(beeLine.loc("no-method",
          new Object[] {method, c.getName()}));
    }

    //看方法的参数集合 找到要执行的方法
    for (Iterator<Method> i = candidateMethods.iterator(); i.hasNext();) {//循环每一个匹配的方法
      Method meth = i.next();
      Class[] ptypes = meth.getParameterTypes();//方法的参数集合
      if (!(ptypes.length == args.size())) {//找到方法参数与给定的参数数量相同的
        continue;
      }

      Object[] converted = convert(args, ptypes);//类型转换,因为给定的参数是object的,因为要转换成具体的对象
      if (converted == null) {
        continue;
      }

      if (!Modifier.isPublic(meth.getModifiers())) {//方法一定是public的
        continue;
      }
      return meth.invoke(on, converted);//执行给定的方法
    }
    return null;
  }


  public static Object[] convert(List objects, Class[] toTypes)
      throws ClassNotFoundException {
    Object[] converted = new Object[objects.size()];
    for (int i = 0; i < converted.length; i++) {//对每一个具体的参数值进行转换
      converted[i] = convert(objects.get(i), toTypes[i]);
    }
    return converted;
  }

    /**
     *
     * @param ob 传入的参数值,具体的值
     * @param toType 该参数在定义方法的时候设置的类型
     * @return 返回 toType指定的类型----即类型转换
     */
  public static Object convert(Object ob, Class toType)
      throws ClassNotFoundException {
    if (ob == null || ob.toString().equals("null")) {
      return null;
    }
    if (toType == String.class) {
      return new String(ob.toString());
    } else if (toType == Byte.class || toType == byte.class) {
      return new Byte(ob.toString());
    } else if (toType == Character.class || toType == char.class) {
      return new Character(ob.toString().charAt(0));
    } else if (toType == Short.class || toType == short.class) {
      return new Short(ob.toString());
    } else if (toType == Integer.class || toType == int.class) {
      return new Integer(ob.toString());
    } else if (toType == Long.class || toType == long.class) {
      return new Long(ob.toString());
    } else if (toType == Double.class || toType == double.class) {
      return new Double(ob.toString());
    } else if (toType == Float.class || toType == float.class) {
      return new Float(ob.toString());
    } else if (toType == Boolean.class || toType == boolean.class) {
      return new Boolean(ob.toString().equals("true")
          || ob.toString().equals(true + "")
          || ob.toString().equals("1")
          || ob.toString().equals("on")
          || ob.toString().equals("yes"));
    } else if (toType == Class.class) {
      return Class.forName(ob.toString());
    }
    return null;
  }
}
