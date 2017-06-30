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

import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.io.IOContext;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.plan.FilterDesc;
import org.apache.hadoop.hive.ql.plan.api.OperatorType;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.io.LongWritable;

/**
 * Filter operator implementation.
 **/
public class FilterOperator extends Operator<FilterDesc> implements
    Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Counter.
   *
   */
  public static enum Counter {
    FILTERED, //��ʾ���˵�,��ͨ��
    PASSED//˵���ж�true,��ʾͨ��
  }

  private final transient LongWritable filtered_count, passed_count;
  private transient ExprNodeEvaluator conditionEvaluator;//�ж��������ʽ
  private transient PrimitiveObjectInspector conditionInspector;//���ʽ�ķ���ֵ����
  private transient int consecutiveFails;//�������ֶ��ٴ���false��ʱ��
  private transient int consecutiveSearches;
  private transient IOContext ioContext;
  transient int heartbeatInterval;//�������

  public FilterOperator() {
    super();
    filtered_count = new LongWritable();
    passed_count = new LongWritable();
    consecutiveFails = 0;
    consecutiveSearches = 0;
  }

  @Override
  protected void initializeOp(Configuration hconf) throws HiveException {
    try {
      heartbeatInterval = HiveConf.getIntVar(hconf,
          HiveConf.ConfVars.HIVESENDHEARTBEAT);//�������
      conditionEvaluator = ExprNodeEvaluatorFactory.get(conf.getPredicate());//�ж��������ʽ
      if (HiveConf.getBoolVar(hconf, HiveConf.ConfVars.HIVEEXPREVALUATIONCACHE)) {//�Ƿ���Ҫ����
        conditionEvaluator = ExprNodeEvaluatorFactory.toCachedEval(conditionEvaluator);
      }

      statsMap.put(Counter.FILTERED, filtered_count);
      statsMap.put(Counter.PASSED, passed_count);
      conditionInspector = null;
      ioContext = IOContext.get();
    } catch (Throwable e) {
      throw new HiveException(e);
    }
    initializeChildren(hconf);
  }

    /**
     *
     * @param row ��ʾ�����һ������
     *          The object representing the row.
     * @param tag
     *          The tag of the row usually means which parent this row comes from.
     *          Rows with the same tag should have exactly the same rowInspector
     * @throws HiveException
     */
  @Override
  public void processOp(Object row, int tag) throws HiveException {
    ObjectInspector rowInspector = inputObjInspectors[tag];
    if (conditionInspector == null) {
      conditionInspector = (PrimitiveObjectInspector) conditionEvaluator
          .initialize(rowInspector);//��ʼ�����ʽ������
    }

    // If the input is sorted, and we are executing a search based on the arguments to this filter,
    // set the comparison in the IOContext and the type of the UDF
    if (conf.isSortedFilter() && ioContext.useSorted()) {
      if (!(conditionEvaluator instanceof ExprNodeGenericFuncEvaluator)) {
        LOG.error("Attempted to use the fact data is sorted when the conditionEvaluator is not " +
                  "of type ExprNodeGenericFuncEvaluator");
        ioContext.setUseSorted(false);
        return;
      } else {
        ioContext.setComparison(((ExprNodeGenericFuncEvaluator)conditionEvaluator).compare(row));
      }

      if (ioContext.getGenericUDFClassName() == null) {
        ioContext.setGenericUDFClassName(
            ((ExprNodeGenericFuncEvaluator)conditionEvaluator).genericUDF.getClass().getName());
      }

      // If we are currently searching the data for a place to begin, do not return data yet
      if (ioContext.isBinarySearching()) {
        consecutiveSearches++;
        // In case we're searching through an especially large set of data, send a heartbeat in
        // order to avoid timeout
        if (((consecutiveSearches % heartbeatInterval) == 0) && (reporter != null)) {
          reporter.progress();
        }
        return;
      }
    }

    Object condition = conditionEvaluator.evaluate(row);//��ȡ���ʽ��ֵ

    // If we are currently performing a binary search on the input, don't forward the results
    // Currently this value is set when a query is optimized using a compact index.  The map reduce
    // job responsible for scanning and filtering the index sets this value.  It remains set
    // throughout the binary search executed by the HiveBinarySearchRecordResder until a starting
    // point for a linear scan has been identified, at which point this value is unset.
    if (ioContext.isBinarySearching()) {
      return;
    }

    Boolean ret = (Boolean) conditionInspector
        .getPrimitiveJavaObject(condition);//ǿ��ת����booleanֵ
    if (Boolean.TRUE.equals(ret)) {//˵��ͨ��
      forward(row, rowInspector);
      passed_count.set(passed_count.get() + 1);
      consecutiveFails = 0;//�������ֶ��ٴ���false��ʱ��
    } else {//˵����ͨ��
      filtered_count.set(filtered_count.get() + 1);
      consecutiveFails++;

      // In case of a lot of consecutive failures, send a heartbeat in order to
      // avoid timeout
      if (((consecutiveFails % heartbeatInterval) == 0) && (reporter != null)) {//���б���
        reporter.progress();
      }
    }
  }

  /**
   * @return the name of the operator
   */
  @Override
  public String getName() {
    return getOperatorName();
  }

  static public String getOperatorName() {
    return "FIL";
  }

  @Override
  public OperatorType getType() {
    return OperatorType.FILTER;
  }

  @Override
  public boolean supportSkewJoinOptimization() {
    return true;
  }

  @Override
  public boolean columnNamesRowResolvedCanBeObtained() {
    return true;
  }

  @Override
  public boolean supportAutomaticSortMergeJoin() {
    return true;
  }

  @Override
  public boolean supportUnionRemoveOptimization() {
    return true;
  }
}
