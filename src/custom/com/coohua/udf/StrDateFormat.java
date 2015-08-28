package com.coohua.udf;

import java.text.SimpleDateFormat;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * ʵ��stringת��Ϊ��Ӧ�����ڸ�ʽyyyy-MM-dd,��ΪhiveĬ��ֻ֧��yyyy-MM-dd��ʽ,Ĭ�ϲ�����ʽ��yyyyMMdd
 * create temporary function  strDateFormat as 'com.coohua.udf.StrDateFormat';
 */
@Description(name = "strDateFormat",
value = "_FUNC_(date1,date1Format) -",
extended = "Example:\n"
+ "  > SELECT strDateFormat('20150101','yyyyMMdd') FROM src LIMIT 1;\n" + "  return 2015-01-01"
+ "  > SELECT strDateFormat('20150101') FROM src LIMIT 1;\n" + "  return 2015-01-01")
public class StrDateFormat extends UDF{
	  
	  private final String sourceStr = "yyyyMMdd";
	  private final Text sourceText = new Text(sourceStr);
	  private final Text lastSourceText = new Text(sourceStr);
	  private  SimpleDateFormat sourceFormatter = new SimpleDateFormat(sourceStr);
	  private final SimpleDateFormat resultFormatter = new SimpleDateFormat("yyyy-MM-dd");
	  
	  Text result = new Text();
	  
	  public StrDateFormat() {
		  
	  }

	  public Text evaluate(Text date) {
		  return evaluate(date,sourceText);
	  }
	  
	  public Text evaluate(Text date,Text dateFormat) {
	    if (date == null || dateFormat == null) {
	      return null;
	    }
	    
	    if(!lastSourceText.equals(dateFormat)){
	    	lastSourceText.set(dateFormat);
	    	sourceFormatter = new SimpleDateFormat(dateFormat.toString());
	    }
	    
	    try{
	    	result.set(resultFormatter.format(sourceFormatter.parse(date.toString())));
	    	return result;
	    }catch(Exception ex){
	    	return null;
	    }
	  }
}
