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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.ErrorMsg;
import org.apache.hadoop.hive.ql.exec.FunctionRegistry;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.Utilities;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.hadoop.hive.ql.session.SessionState.LogHelper;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFBaseCompare;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFBridge;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;

/**
 * Describes a GenericFunc node.
 */
public class ExprNodeGenericFuncDesc extends ExprNodeDesc implements
    Serializable {

  private static final long serialVersionUID = 1L;

  private static final Log LOG = LogFactory
      .getLog(ExprNodeGenericFuncDesc.class.getName());

  /**
   * In case genericUDF is Serializable, we will serialize the object.
   *
   * In case genericUDF does not implement Serializable, Java will remember the
   * class of genericUDF and creates a new instance when deserialized. This is
   * exactly what we want.
   */
  private GenericUDF genericUDF;//自定义函数
  private List<ExprNodeDesc> childExprs;//自定义函数的参数集合
  /**
   * This class uses a writableObjectInspector rather than a TypeInfo to store
   * the canonical type information for this NodeDesc.
   */
  private transient ObjectInspector writableObjectInspector;//函数返回值
  //Is this an expression that should perform a comparison for sorted searches 是否执行一个排序
  private boolean isSortedExpr;

  public ExprNodeGenericFuncDesc() {
  }

  public ExprNodeGenericFuncDesc(TypeInfo typeInfo, GenericUDF genericUDF,
      List<ExprNodeDesc> children) {
    this(TypeInfoUtils.getStandardWritableObjectInspectorFromTypeInfo(typeInfo),
         genericUDF, children);
  }

  /**
   * 
   * @param oi 函数返回值
   * @param genericUDF 自定义函数
   * @param children 函数所需要的参数集合
   */
  public ExprNodeGenericFuncDesc(ObjectInspector oi, GenericUDF genericUDF,
      List<ExprNodeDesc> children) {
    super(TypeInfoUtils.getTypeInfoFromObjectInspector(oi));
    this.writableObjectInspector =
        ObjectInspectorUtils.getWritableObjectInspector(oi);
    assert (genericUDF != null);
    this.genericUDF = genericUDF;
    this.childExprs = children;
  }

  @Override
  public ObjectInspector getWritableObjectInspector() {
    return writableObjectInspector;
  }

  public GenericUDF getGenericUDF() {
    return genericUDF;
  }

  public void setGenericUDF(GenericUDF genericUDF) {
    this.genericUDF = genericUDF;
  }

  public List<ExprNodeDesc> getChildExprs() {
    return childExprs;
  }

  public void setChildExprs(List<ExprNodeDesc> children) {
    childExprs = children;
  }

  @Override
  public List<ExprNodeDesc> getChildren() {
    return childExprs;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(genericUDF.getClass().toString());
    sb.append("(");
    for (int i = 0; i < childExprs.size(); i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(childExprs.get(i).toString());
    }
    sb.append("(");
    sb.append(")");
    return sb.toString();
  }

  @Explain(displayName = "expr")
  @Override
  public String getExprString() {
    // Get the children expr strings
    String[] childrenExprStrings = new String[childExprs.size()];
    for (int i = 0; i < childrenExprStrings.length; i++) {
      childrenExprStrings[i] = childExprs.get(i).getExprString();
    }

    return genericUDF.getDisplayString(childrenExprStrings);
  }

  @Override
  public List<String> getCols() {
    List<String> colList = new ArrayList<String>();
    if (childExprs != null) {
      int pos = 0;
      while (pos < childExprs.size()) {
        List<String> colCh = childExprs.get(pos).getCols();
        colList = Utilities.mergeUniqElems(colList, colCh);
        pos++;
      }
    }

    return colList;
  }

  @Override
  public ExprNodeDesc clone() {
    List<ExprNodeDesc> cloneCh = new ArrayList<ExprNodeDesc>(childExprs.size());
    for (ExprNodeDesc ch : childExprs) {
      cloneCh.add(ch.clone());
    }
    ExprNodeGenericFuncDesc clone = new ExprNodeGenericFuncDesc(typeInfo,
        FunctionRegistry.cloneGenericUDF(genericUDF), cloneCh);
    return clone;
  }

  /**
   * Create a exprNodeGenericFuncDesc based on the genericUDFClass and the
   * children parameters.
   *
   * @throws UDFArgumentException
   */
  public static ExprNodeGenericFuncDesc newInstance(GenericUDF genericUDF,
      List<ExprNodeDesc> children) throws UDFArgumentException {
    ObjectInspector[] childrenOIs = new ObjectInspector[children.size()];
    for (int i = 0; i < childrenOIs.length; i++) {
      childrenOIs[i] = children.get(i).getWritableObjectInspector();
    }

    // Check if a bigint is implicitely cast to a double as part of a comparison
    // Perform the check here instead of in GenericUDFBaseCompare to guarantee it is only run once per operator
    //是两个参数,可以进行比较,例如a op b,比较a与b的顺序
    if (genericUDF instanceof GenericUDFBaseCompare && children.size() == 2) {

      TypeInfo oiTypeInfo0 = children.get(0).getTypeInfo();
      TypeInfo oiTypeInfo1 = children.get(1).getTypeInfo();

      SessionState ss = SessionState.get();
      Configuration conf = (ss != null) ? ss.getConf() : new Configuration();

      LogHelper console = new LogHelper(LOG);

      // For now, if a bigint is going to be cast to a double throw an error or warning
      //对long类型和String类型比较、对long类型和double类型比较的时候,是要有警告或者严格约束抛异常的
      //原因会有丢失精度的可能
      if ((oiTypeInfo0.equals(TypeInfoFactory.stringTypeInfo) && oiTypeInfo1.equals(TypeInfoFactory.longTypeInfo)) ||
          (oiTypeInfo0.equals(TypeInfoFactory.longTypeInfo) && oiTypeInfo1.equals(TypeInfoFactory.stringTypeInfo))) {
        if (HiveConf.getVar(conf, HiveConf.ConfVars.HIVEMAPREDMODE).equalsIgnoreCase("strict")) {
          throw new UDFArgumentException(ErrorMsg.NO_COMPARE_BIGINT_STRING.getMsg());
        } else {
          console.printError("WARNING: Comparing a bigint and a string may result in a loss of precision.");
        }
      } else if ((oiTypeInfo0.equals(TypeInfoFactory.doubleTypeInfo) && oiTypeInfo1.equals(TypeInfoFactory.longTypeInfo)) ||
          (oiTypeInfo0.equals(TypeInfoFactory.longTypeInfo) && oiTypeInfo1.equals(TypeInfoFactory.doubleTypeInfo))) {
        if (HiveConf.getVar(conf, HiveConf.ConfVars.HIVEMAPREDMODE).equalsIgnoreCase("strict")) {
          throw new UDFArgumentException(ErrorMsg.NO_COMPARE_BIGINT_DOUBLE.getMsg());
        } else {
          console.printError("WARNING: Comparing a bigint and a double may result in a loss of precision.");
        }
      }
    }

    //通过参数初始化函数,并且返回函数的返回值
    ObjectInspector oi = genericUDF.initializeAndFoldConstants(childrenOIs);

    //加载自定义函数是否依赖外部资源
    String[] requiredJars = genericUDF.getRequiredJars();
    String[] requiredFiles = genericUDF.getRequiredFiles();
    SessionState ss = SessionState.get();

    if (requiredJars != null) {
      SessionState.ResourceType t = SessionState.find_resource_type("JAR");
      for (String jarPath : requiredJars) {
        ss.add_resource(t, jarPath);
      }
    }

    if (requiredFiles != null) {
      SessionState.ResourceType t = SessionState.find_resource_type("FILE");
      for (String filePath : requiredFiles) {
        ss.add_resource(t, filePath);
      }
    }

    return new ExprNodeGenericFuncDesc(oi, genericUDF, children);
  }

  @Override
  public boolean isSame(Object o) {
    if (!(o instanceof ExprNodeGenericFuncDesc)) {
      return false;
    }
    ExprNodeGenericFuncDesc dest = (ExprNodeGenericFuncDesc) o;
    if (!typeInfo.equals(dest.getTypeInfo())
        || !genericUDF.getClass().equals(dest.getGenericUDF().getClass())) {
      return false;
    }

    if (genericUDF instanceof GenericUDFBridge) {
      GenericUDFBridge bridge = (GenericUDFBridge) genericUDF;
      GenericUDFBridge bridge2 = (GenericUDFBridge) dest.getGenericUDF();
      if (!bridge.getUdfClass().equals(bridge2.getUdfClass())
          || !bridge.getUdfName().equals(bridge2.getUdfName())
          || bridge.isOperator() != bridge2.isOperator()) {
        return false;
      }
    }

    if (childExprs.size() != dest.getChildExprs().size()) {
      return false;
    }

    for (int pos = 0; pos < childExprs.size(); pos++) {
      if (!childExprs.get(pos).isSame(dest.getChildExprs().get(pos))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    int superHashCode = super.hashCode();
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.appendSuper(superHashCode);
    builder.append(childExprs);
    return builder.toHashCode();
  }

  public boolean isSortedExpr() {
    return isSortedExpr;
  }

  public void setSortedExpr(boolean isSortedExpr) {
    this.isSortedExpr = isSortedExpr;
  }
}
