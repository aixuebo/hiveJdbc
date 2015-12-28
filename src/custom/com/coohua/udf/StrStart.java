package com.coohua.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string的startsWith方法
 * true返回true,false返回false
 * create temporary function  strStart as 'com.coohua.udf.StrStart';  
 */
@Description(name = "strStart",
value = "_FUNC_(str,prefix) - true return true,false return false ",
extended = "Example:\n"
+ "  > SELECT strStart('abcd','ab') FROM src LIMIT 1;\n" + "  return true")
public class StrStart extends UDF{
	
	Text result = new Text();

	  public StrStart() {
	  }

	  public Text evaluate(Text s,Text prefix) {
	    if (s == null) {
	      result.set("false");
	      return result;
	    }
	    result.set(s.toString().startsWith(prefix.toString()) ? "true":"false");
	    return result;
	  }
}
