package maming.antlr;

import org.junit.Test;

public class TestRun {

	@Test
	public void test1(){
		String path = "E://tmp//hive//Expr.g";
		//new org.antlr.Tool(new String[] {path});
		
		new org.antlr.works.IDE();
	}
	
	public static void main(String[] args) {
		new org.antlr.works.IDE();
	}
}
