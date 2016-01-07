package maming.antlr;

import java.io.IOException;
import java.util.Enumeration;


import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;
import org.apache.hadoop.hive.ql.parse.SemanticAnalyzer;
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
	
	@Test
	public void test5(){
		StringBuffer command = new StringBuffer();
		command.append("SELECT * FROM exampleTable ")
			   .append(" LATERAL VIEW explode(col1) myTable1 AS myCol1 ")
		       .append(" LATERAL VIEW explode(myCol1) myTable2 AS myCol2 ");
		try {
			ASTNode node = new ParseDriver().parse(command.toString());
			System.out.println(node.dump());
			System.out.println(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test6(){
		
		StringBuffer command = new StringBuffer();
		command.append("alter table maming.statisticnginx add partition (log_day='20151101') location '/logs/statistics/statistics_hadoop/statisticNginx/20151101';");
		try {
			ASTNode node = new ParseDriver().parse(command.toString());
			System.out.println(node.dump());
			System.out.println(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
