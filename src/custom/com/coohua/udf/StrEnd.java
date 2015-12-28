package com.coohua.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string的endsWith方法
 * true返回true,false返回false
 * create temporary function  strEnd as 'com.coohua.udf.StrEnd';  
 */
@Description(name = "strEnd",
value = "_FUNC_(str,suffer) - true return true,false return false ",
extended = "Example:\n"
+ "  > SELECT strEnd('abcd','cd') FROM src LIMIT 1;\n" + "  return true")
public class StrEnd extends UDF{
	
	Text result = new Text();

	  public StrEnd() {
	  }

	  public Text evaluate(Text s,Text prefix) {
	    if (s == null) {
	      result.set("false");
	      return result;
	    }
	    result.set(s.toString().endsWith(prefix.toString()) ? "true":"false");
	    return result;
	  }
}
