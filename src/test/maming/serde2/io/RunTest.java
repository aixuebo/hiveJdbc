package maming.serde2.io;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class RunTest {

	@Test
	public void test1(){
		BigDecimal d = BigDecimal.ZERO;
		d = new BigDecimal(5.987);
		System.out.println(d.scale());//小数点后面的位数,5.9870000000000000994759830064140260219573974609375 后面是49位,因此显示结果为49
		d = d.setScale(17,BigDecimal.ROUND_DOWN);//产生一个新的BigDecimal对象
		//d.setScale(19);//有异常
		System.out.println(d.precision());
		System.out.println(d.scale());
		System.out.println(d);
		System.out.println("9870000000000000994759830064140260219573974609375".length());
		System.out.println("9870000000000000".length());
	}
	
	@Test
	public void test2(){
		BigDecimal d = BigDecimal.ZERO;
		//d = new BigDecimal(15.987);
		d = new BigDecimal(0.0987);
		System.out.println(d.precision());//51
		System.out.println(d.scale());//49,少了15两个字符
		
	    int valuePrecision = d.precision()
	            + Math.max(0, 1 + d.scale() - d.precision());
	    System.out.println(valuePrecision);
	    
	    System.out.println(d.toPlainString());
	    
	    System.out.println(d.unscaledValue());
	    
	    
	    BigDecimal dx = new BigDecimal(new BigInteger(d.unscaledValue().toByteArray()), d.scale());
	    System.out.println(dx);
	    System.out.println(dx.toPlainString());
		
	}
	
	@Test
	public void test3(){
		String altValue = "1";
		System.out.println(Byte.valueOf(altValue).byteValue());
	}
}
