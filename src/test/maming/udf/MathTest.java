package maming.udf;

import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class MathTest {

	@Test
	public void test1(){
		System.out.println(Math.PI);//pi��ֵ
		
		System.out.println(Math.E);//e��ֵ
		System.out.println(Math.exp(1));//e�ļ��η�
		System.out.println(Math.exp(2));
		
		System.out.println(Math.abs(-5.3));//��ȡ����,����5.3
		
		System.out.println((long)Math.ceil(5.3));//����6.0,����ȡ��
		
		System.out.println(Math.floor(5.3));//
		
	}
	
/*	@Test
	public void test2(){
		ArrayList<String>  aList=new ArrayList<String>();
		aList.add("a");
		aList.add("b");
		aList.add("d");
		ListIterator<String> it = (ListIterator<String>) aList.listIterator();
		aList.add("x");           // ��������ķ���ȥ�޸�����
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
