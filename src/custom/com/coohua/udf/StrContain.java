package com.coohua.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 实现string的contains方法
 * true返回1,false返回0 
 * create temporary function  strContain as 'com.coohua.udf.StrContain'; 
 */
@Description(name = "strContains",
value = "_FUNC_(str,strContain) - true return true,false return false ",
extended = "Example:\n"
+ "  > SELECT strContain('abcd','bc') FROM src LIMIT 1;\n" + "  return true")
public class StrContain extends UDF{
	
	  Text result = new Text();

	  public StrContain() {
	  }

	  public Text evaluate(Text s,Text contain) {
	    if (s == null) {
	      result.set("false");
	      return result;
	    }
	    result.set(s.toString().contains(contain.toString()) ? "true":"false");
	    return result;
	  }
}
