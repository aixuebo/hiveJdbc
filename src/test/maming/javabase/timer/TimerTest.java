package maming.javabase.timer;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimerTest {

	@Test
	public void test1(){
		long duration = 100000;
		long xx = TimeUnit.DAYS.toMillis(duration);//��ʾ��100000��,ת���ɺ�����Ҫ���ٺ��� ,���8640000000000
		System.out.println(xx);
		xx = TimeUnit.DAYS.convert(5000, TimeUnit.HOURS);//��ʾ5000Сʱ,����ת���ɶ�����,���5000/24 = 208��
		System.out.println(xx);
	}
}
