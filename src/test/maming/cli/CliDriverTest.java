package maming.cli;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.cli.CliDriver;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.CommandNeedRetryException;
import org.apache.hadoop.hive.ql.Driver;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.junit.Test;

public class CliDriverTest {

	@Test
	public void test1() {
		String line = "test1;test2;aa\\dd\\ee\\;";
		String command = "";
		for (String oneCmd : line.split(";")) {
			if (StringUtils.endsWith(oneCmd, "\\")) {// 说明命令还没有输入完成,还可以继续输入
				command += StringUtils.chop(oneCmd) + ";";
				continue;
			} else {
				command += oneCmd;
			}
		}
		System.out.println(command);
	}
	
	  public static String spacesForString(String s) {
		    if (s == null || s.length() == 0) {
		      return "";
		    }
		    System.out.println("%1$-" + s.length() +"s");
		    return String.format("%1$-" + s.length() +"s", "");
		  }
	  
	  @Test
	  public void test2(){
/*		  String message = "abc";
		  System.out.println(spacesForString(message));*/
		  
		  System.out.println(Integer.MAX_VALUE);
	  }
	  
	  @Test
	  public void test3(){
		  HiveConf conf = new HiveConf();
		  SessionState.start(conf);
		  StringBuffer command = new StringBuffer();
			command.append("select distinct biao11.id,biao11.name,biao11.age,biao11.sex from ")
		       .append(" (select * from biao1) biao11")
		       .append(" where biao11.id = 100 ");
		  Driver driver = new Driver(conf);
		  try {
			driver.run(command.toString());
		} catch (CommandNeedRetryException e) {
			e.printStackTrace();
		}
	  }
	  
	  @Test
	  public void test4(){
		  String[] args = {"hive"};
		    int ret;
			try {
				ret = new CliDriver().run(args);
			    System.exit(ret);
			} catch (Exception e) {
				e.printStackTrace();
			}

	  }
}
