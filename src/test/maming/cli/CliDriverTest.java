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
			if (StringUtils.endsWith(oneCmd, "\\")) {// ˵�����û���������,�����Լ�������
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
		  String message = "abc";
		  System.out.println(spacesForString(message));
	  }
	  
	  @Test
	  public void test3(){
		  HiveConf conf = new HiveConf();
		  SessionState.start(conf);
		  String command = "select * from aa";
		  Driver driver = new Driver(conf);
		  try {
			driver.run(command);
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
