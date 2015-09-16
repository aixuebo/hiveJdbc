package maming;

import org.junit.Test;

public class Test1 {

	@Test
	public void test1(){
		int a = 2;
		switch(a){
			case 1:
				System.out.println("1");
			case 2:	
				System.out.println("2");
			case 3:
				System.out.println("3");
				break;	
			case 6:		
				System.out.println("6");
				break;	
		}
	}
}
