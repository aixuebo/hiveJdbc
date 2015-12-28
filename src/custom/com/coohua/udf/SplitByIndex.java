package com.coohua.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * ʵ��string��ͨ��split������ֳ������.��ȡ�ڼ���Ԫ�ص�ֵ,��0��ʼ����
 * create temporary function  split_by_index as 'com.coohua.udf.SplitByIndex';  
 */
@Description(name = "split_by_index",
value = "_FUNC_(str,prefix) - true return true,false return false ",
extended = "Example:\n"
+ "  > SELECT split_by_index('abcd|xyz','\\|',1) FROM src LIMIT 1;\n" + "  return xyz")
public class SplitByIndex extends UDF{

	Text defaultResult = new Text(); //Ĭ��ֵ
	Text result = new Text();
	IntWritable index = new IntWritable(0);
	
	  public SplitByIndex() {
	  }

	  public Text evaluate(Text s,Text split,IntWritable index,Text defaultResult) {
		  	this.defaultResult = defaultResult;
		    if (s == null) {
		      return defaultResult;
		    }
		    String[] arr = s.toString().split(split.toString());
		    if(arr.length <= index.get()){
		    	return defaultResult;
		    }
		    result.set(arr[index.get()]);
		    return result;
		  }
	  
	  public Text evaluate(Text s,Text split,IntWritable index) {
	    if (s == null) {
	      return defaultResult;
	    }
	    String[] arr = s.toString().split(split.toString());
	    if(arr.length <= index.get()){
	    	return defaultResult;
	    }
	    result.set(arr[index.get()]);
	    return result;
	  }
}
