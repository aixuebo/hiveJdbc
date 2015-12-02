package com.bigdata.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    public static final DateFormat YMD_SINGLE = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat YMDH_SINGLE = new SimpleDateFormat("yyyyMMdd:HH");
    public static final SimpleDateFormat SDF_ALL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    public static final DateFormat YMD_Middler_SINGLE = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 返回该日期对应的星期几
     *
     * @param date 格式为:yyyyMMdd
     * @return
     */
    public static int weekday(String date) {
        try {
            int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
            Calendar cal = Calendar.getInstance();
            cal.setTime(YMD_SINGLE.parse(date));
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 此方法线程不安全
     *
     * @param formatStr: 日期格式 yyyyMMddHHmm
     * @param type       Calendar.HOUR_OF_DAY 小时 ;Calendar.MINUTE 分钟
     * @param minute
     * @return 根据单位和具体数值进行日期转换
     */
    public static String dateConvertSingle(String formatStr, int type, int minute) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Calendar nowDate = Calendar.getInstance();
        nowDate.add(type, minute);
        return format.format(nowDate.getTime());
    }

    /**
     * 此方法线程不安全
     */
    public static String dateConvertSingle(int type, int minute) {
        Calendar nowDate = Calendar.getInstance();
        nowDate.add(type, minute);
        return YMD_SINGLE.format(nowDate.getTime());
    }

    public static String dateConvertSingleByDay(String date, int dayNum) {
        Calendar nowDate = Calendar.getInstance();
        try {
            nowDate.setTime(YMD_SINGLE.parse(date));
            nowDate.add(Calendar.DAY_OF_YEAR, dayNum);
            return YMD_SINGLE.format(nowDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("dateConvertSingleByDay方法,时间转化异常" + date);
        }
    }

    public static void main(String[] args) {
        String date = "20141016";
        String before30Day = DateUtil.dateConvertSingleByDay(date, -29);//计算前30天的日期
        String before7Day = DateUtil.dateConvertSingleByDay(date, -6);//计算前7天的日期
        String before3Day = DateUtil.dateConvertSingleByDay(date, -2);//计算前3天的日期

        System.out.println(before30Day);
        System.out.println(before7Day);
        System.out.println(before3Day);

        System.out.println(DateUtil.weekday("20141203"));
        
    }
}
