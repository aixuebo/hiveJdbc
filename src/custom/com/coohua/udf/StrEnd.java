package com.coohua.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string的endsWith方法
 * true返回1,false返回0
 * create temporary function  strEnd as 'com.coohua.udf.StrEnd';  
 */
@Description(name = "strEnd",
value = "_FUNC_(str,suffer) - true return 1,false return 0 ",
extended = "Example:\n"
+ "  > SELECT strEnd('abcd','cd') FROM src LIMIT 1;\n" + "  return 1")
public class StrEnd extends UDF{
	
	Text result = new Text();

	  public StrEnd() {
	  }

	  public Text evaluate(Text s,Text prefix) {
	    if (s == null) {
	      result.set("0");
	      return result;
	    }
	    result.set(s.toString().endsWith(prefix.toString()) ? "1":"0");
	    return result;
	  }
}
