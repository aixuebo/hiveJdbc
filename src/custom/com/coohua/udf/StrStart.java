package com.coohua.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * ʵ��string��startsWith����
 * true����1,false����0
 * create temporary function  strStart as 'com.coohua.udf.StrStart';  
 */
@Description(name = "strStart",
value = "_FUNC_(str,prefix) - true return 1,false return 0 ",
extended = "Example:\n"
+ "  > SELECT strStart('abcd','ab') FROM src LIMIT 1;\n" + "  return 1")
public class StrStart extends UDF{
	
	Text result = new Text();

	  public StrStart() {
	  }

	  public Text evaluate(Text s,Text prefix) {
	    if (s == null) {
	      result.set("0");
	      return result;
	    }
	    result.set(s.toString().startsWith(prefix.toString()) ? "1":"0");
	    return result;
	  }
}
