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

package org.apache.hadoop.hive.ql.udf.generic;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.exec.WindowFunctionDescription;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator.AggregationBuffer;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils.ObjectInspectorCopyOption;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.IntWritable;

@WindowFunctionDescription
(
		description = @Description(
								name = "rank",
								value = "_FUNC_(x)"
								),
		supportsWindow = false,
		pivotResult = true,
		rankingFunction = true,
		impliesOrder = true
)
/**
 * RANK() OVER(PARTITION BY cookieid ORDER BY pv desc) AS rn1,
 * 按照 cookieid分组,组内的所有pv进行大小比较,为每一个比较后的值分配唯一的序号,但是注意:如果遇到两个比较值相同的,则会在名次中留下空位
 */
public class GenericUDAFRank extends AbstractGenericUDAFResolver
{
	static final Log LOG = LogFactory.getLog(GenericUDAFRank.class.getName());

	//校验参数,必须大于1,并且所以参数都必须支持比较
	@Override
	public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException
	{
		if (parameters.length < 1)
		{
			throw new UDFArgumentTypeException(parameters.length - 1, "One or more arguments are expected.");
		}
		for(int i=0; i<parameters.length; i++)
		{
			//每一个参数对象都必须支持可比较
			ObjectInspector oi = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(parameters[i]);
			if (!ObjectInspectorUtils.compareSupported(oi))
			{
				throw new UDFArgumentTypeException(i,
					"Cannot support comparison of map<> type or complex type containing map<>.");
			}
		}
		return createEvaluator();
	}

	protected GenericUDAFRankEvaluator createEvaluator()
	{
		return new GenericUDAFRankEvaluator();
	}

	static class RankBuffer implements AggregationBuffer
	{
		ArrayList<IntWritable> rowNums;
		int currentRowNum;
		Object[] currVal;
		int currentRank;
		int numParams;

		RankBuffer(int numParams)
		{
			this.numParams = numParams;
			init();
		}

		void init()
		{
			rowNums = new ArrayList<IntWritable>();
			currentRowNum = 0;
			currentRank = 0;
			currVal = new Object[numParams];
		}

		void incrRowNum() { currentRowNum++; }

		void addRank()
		{
			rowNums.add(new IntWritable(currentRank));
		}
	}

	public static class GenericUDAFRankEvaluator extends GenericUDAFEvaluator
	{
		ObjectInspector[] inputOI;
		ObjectInspector[] outputOI;

		@Override
		public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException
		{
			super.init(m, parameters);
			if (m != Mode.COMPLETE)
			{
				throw new HiveException(
						"Only COMPLETE mode supported for Rank function");
			}
			inputOI = parameters;
			outputOI = new ObjectInspector[inputOI.length];
			for(int i=0; i < inputOI.length; i++)
			{
				outputOI[i] = ObjectInspectorUtils.getStandardObjectInspector(inputOI[i], ObjectInspectorCopyOption.JAVA);
			}
			return ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.writableIntObjectInspector);
		}

		@Override
		public AggregationBuffer getNewAggregationBuffer() throws HiveException
		{
			return new RankBuffer(inputOI.length);
		}

		@Override
		public void reset(AggregationBuffer agg) throws HiveException
		{
			((RankBuffer) agg).init();
		}

		@Override
		public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException
		{
			RankBuffer rb = (RankBuffer) agg;
			 int c = GenericUDAFRank.compare(rb.currVal, outputOI, parameters, inputOI);
			 rb.incrRowNum();
			if ( rb.currentRowNum == 1 || c != 0 )
			{
				nextRank(rb);
				rb.currVal = GenericUDAFRank.copyToStandardObject(parameters, inputOI, ObjectInspectorCopyOption.JAVA);
			}
			rb.addRank();
		}

		/*
		 * Called when the value in the partition has changed. Update the currentRank
		 */
		protected void nextRank(RankBuffer rb)
		{
			rb.currentRank = rb.currentRowNum;
		}

		@Override
		public Object terminatePartial(AggregationBuffer agg) throws HiveException
		{
			throw new HiveException("terminatePartial not supported");
		}

		@Override
		public void merge(AggregationBuffer agg, Object partial) throws HiveException
		{
			throw new HiveException("merge not supported");
		}

		@Override
		public Object terminate(AggregationBuffer agg) throws HiveException
		{
			return ((RankBuffer) agg).rowNums;
		}

	}

  public static int compare(Object[] o1, ObjectInspector[] oi1, Object[] o2,
      ObjectInspector[] oi2)
  {
    int c = 0;
    for (int i = 0; i < oi1.length; i++)
    {
      c = ObjectInspectorUtils.compare(o1[i], oi1[i], o2[i], oi2[i]);
      if (c != 0) {
        return c;
      }
    }
    return c;
  }

  public static Object[] copyToStandardObject(Object[] o,
      ObjectInspector[] oi,
      ObjectInspectorCopyOption objectInspectorOption)
  {
    Object[] out = new Object[o.length];
    for (int i = 0; i < oi.length; i++)
    {
      out[i] = ObjectInspectorUtils.copyToStandardObject(o[i], oi[i],
          objectInspectorOption);
    }
    return out;
  }

}

