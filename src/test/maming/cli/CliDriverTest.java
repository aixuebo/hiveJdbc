package maming.cli;

import org.apache.commons.lang.StringUtils;
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
		  String message = "abc";
		  System.out.println(spacesForString(message));
	  }
}
