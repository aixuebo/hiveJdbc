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
value = "_FUNC_(str,strContain) - true return 1,false return 0 ",
extended = "Example:\n"
+ "  > SELECT strContain('abcd','bc') FROM src LIMIT 1;\n" + "  return 1")
public class StrContain extends UDF{
	
	  Text result = new Text();

	  public StrContain() {
	  }

	  public Text evaluate(Text s,Text contain) {
	    if (s == null) {
	      result.set("0");
	      return result;
	    }
	    result.set(s.toString().contains(contain.toString()) ? "1":"0");
	    return result;
	  }
}
