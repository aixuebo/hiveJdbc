package maming.javabase.timer;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimerTest {

	@Test
	public void test1(){
		long duration = 100000;
		long xx = TimeUnit.DAYS.toMillis(duration);//表示将100000天,转换成毫秒需要多少毫秒 ,输出8640000000000
		System.out.println(xx);
		xx = TimeUnit.DAYS.convert(5000, TimeUnit.HOURS);//表示5000小时,可以转换成多少天,输出5000/24 = 208天
		System.out.println(xx);
	}
}
