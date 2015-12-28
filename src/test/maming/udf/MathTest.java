package maming.udf;

import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class MathTest {

	@Test
	public void test1(){
		System.out.println(Math.PI);//pi的值
		
		System.out.println(Math.E);//e的值
		System.out.println(Math.exp(1));//e的几次方
		System.out.println(Math.exp(2));
		
		System.out.println(Math.abs(-5.3));//获取正数,返回5.3
		
		System.out.println((long)Math.ceil(5.3));//返回6.0,向上取整
		
		System.out.println(Math.floor(5.3));//
		
	}
	
/*	@Test
	public void test2(){
		ArrayList<String>  aList=new ArrayList<String>();
		aList.add("a");
		aList.add("b");
		aList.add("d");
		ListIterator<String> it = (ListIterator<String>) aList.listIterator();
		aList.add("x");           // 调用自身的方法去修改正常
		while(it.hasNext()){
			System.out.print(it.next()+" ");
		}
	}*/
	
	@Test
	public void test3(){
		String str = "    abc 1  ";
		str = StringUtils.stripStart(str, " ");
		System.out.println(str);
	}
}
