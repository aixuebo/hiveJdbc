package maming.antlr;

import java.io.IOException;
import java.util.Enumeration;


import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;
import org.junit.Test;

public class TestRun {

	@Test
	public void test1(){
		//String path = "E://tmp//hive//Expr.g";
		//new org.antlr.Tool(new String[] {path});
		//new org.antlr.works.IDE();
		
		try {
			//Enumeration enumeration = this.getClass().getClassLoader().getResources("/maming/antlr/TestRun");
			Enumeration enumeration = this.getClass().getClassLoader().getResources("./org/apache/hadoop/hive/ql/parse/HiveLexer.class");
			System.out.println("ssssss");
			while(enumeration.hasMoreElements()){
				System.out.println(enumeration.nextElement());
			}
			System.out.println("end");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	@Test
	public void test2(){
		String command = "select distinct aa,bb from biao";
		try {
			ASTNode node = new ParseDriver().parse(command);
			System.out.println(node.dump());
			System.out.println(node);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3(){
		String command = "select distinct aa as (mm,vv),bb from biao";
		try {
			ASTNode node = new ParseDriver().parse(command);
			System.out.println(node.dump());
			System.out.println(node);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4(){
		String command = "select not ee or aa and bb,dd from biao";
		try {
			ASTNode node = new ParseDriver().parse(command);
			System.out.println(node.dump());
			System.out.println(node);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
