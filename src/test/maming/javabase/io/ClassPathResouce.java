package maming.javabase.io;

import org.junit.Assert;
import org.junit.Test;

public class ClassPathResouce {

	//测试class是否在jar包内
	@Test
	public void classInJarTest(){
		String classpath = "/org/apache/commons/cli/BasicParser.class";
		Assert.assertEquals(ClassPathResouce.class.getResource(classpath).toString(),"jar:file:/D:/workspaceHive/hiveJdbc/lib/commons-cli-1.2.jar!/org/apache/commons/cli/BasicParser.class");
		classpath = "/maming/ReadFile.class";
		Assert.assertEquals(ClassPathResouce.class.getResource(classpath).toString(),"file:/D:/workspaceHive/hiveJdbc/bin/maming/ReadFile.class");
	}
}
