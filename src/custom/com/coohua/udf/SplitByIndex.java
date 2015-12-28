package com.coohua.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * 实现string的通过split方法拆分成数组后.获取第几个元素的值,从0开始计算
 * create temporary function  split_by_index as 'com.coohua.udf.SplitByIndex';  
 */
@Description(name = "split_by_index",
value = "_FUNC_(str,prefix) - true return true,false return false ",
extended = "Example:\n"
+ "  > SELECT split_by_index('abcd|xyz','\\|',1) FROM src LIMIT 1;\n" + "  return xyz")
public class SplitByIndex extends UDF{

	Text defaultResult = new Text(); //默认值
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
