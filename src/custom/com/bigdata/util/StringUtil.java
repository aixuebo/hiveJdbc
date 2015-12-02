package com.bigdata.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class StringUtil {

    public static int toInteger(String value, int defaultValue) {
        try {
            if ("".equals(trim(value))) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static int toInteger(String value) {
        return toInteger(value, 0);
    }

    public static long toLong(String value, long defaultValue) {
        try {
            if ("".equals(trim(value))) {
                return defaultValue;
            }
            return Long.parseLong(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static long toLong(String value) {
        return toLong(value, 0l);
    }

    public static String trim(String value, String defaultValue) {
        if (value == null || "".equals(value)) return defaultValue;
        else return value.trim();
    }


    public static String trim(String value) {
        return trim(value, "");
    }

    public static String cut(String value, int len) {
        if (trim(value).length() > len) {
            return value.substring(0, len);
        }
        return trim(value);
    }

    public static String formatDecimal(String decimalString) {
        DecimalFormat df1 = new DecimalFormat("#,###.00");
        return df1.format(div(decimalString, "100").doubleValue());
    }

    public static String formatDecimal(long decimalLong) {
        DecimalFormat df1 = new DecimalFormat("#,###.00");
        return df1.format(div(String.valueOf(decimalLong), "100").doubleValue());
    }

    public static String formatLong(String decimalLong) {
        return formatLong(Long.parseLong(decimalLong));
    }

    public static String formatLong(int decimalLong) {
        return formatLong(String.valueOf(decimalLong));
    }

    public static String formatLong(long decimalLong) {
        DecimalFormat df1 = new DecimalFormat("#,###");
        return df1.format(decimalLong);
    }

    public static BigDecimal div(String value1, String value2) {
        return new BigDecimal(value1).divide(new BigDecimal(value2), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal div(long value1, int value2) {
        return new BigDecimal(value1).divide(new BigDecimal(value2), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static String setStringToString(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = set.iterator();
        if (it.hasNext()) {
            sb.append('\'').append(it.next()).append('\'');
            while (it.hasNext()) {
                sb.append(",'").append(it.next()).append('\'');
            }
        }
        return sb.toString();
    }

    public static String setIntegerToString(Set<Integer> set) {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> it = set.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(',').append(it.next());
            }
        }
        return sb.toString();
    }

    public static String setLongToString(Set<Long> set) {
        StringBuilder sb = new StringBuilder();
        Iterator<Long> it = set.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(',').append(it.next());
            }
        }
        return sb.toString();
    }

    public static String setIntegerArrToString(int[] arrInt) {
        StringBuilder sb = new StringBuilder(String.valueOf(arrInt[0]));
        for (int i = 1; i < arrInt.length; i++) {
            sb.append(',').append(arrInt[i]);
        }
        return sb.toString();
    }

    public static String parseLineForString(String line, String key, String separator, String defaultStr) {
        int indexBegin = line.indexOf(key);
        if (indexBegin == -1) {//不存在key的时候,返回-1
            return defaultStr;
        }
        if (line.indexOf(separator, indexBegin) == -1) {
            separator = "";
        }
        if (!"".equals(separator)) {
            int indexEnd = line.indexOf(separator, indexBegin);
            String str = line.substring(indexBegin + key.length(), indexEnd).trim();
            return str;
        } else {
            String str = line.substring(indexBegin + key.length()).trim();
            return str;
        }
    }

    public static long parseLineForLong(String line, String key, String separator) {
        return Long.parseLong(parseLineForString(line, key, separator, "-1"));
    }

    public static int parseLineForInteger(String line, String key, String separator) {
        return Integer.parseInt(parseLineForString(line, key, separator, "-1"));
    }

    public static String mapToString(Map<String, String> map) {
        String result = map.toString();
        return result.substring(1, result.length() - 1);
    }

    public static Map<String, String> stringToMap(String mapString) {
        Map<String, String> map = new HashMap<String, String>();
        if (mapString == null || "".equals(mapString)) {
            return map;
        }
        String[] keyValues = mapString.split(",");
        String[] tempArr = null;
        for (String keyValue : keyValues) {
            tempArr = keyValue.split("=");
            if (tempArr.length == 2) {
                map.put(tempArr[0].trim(), tempArr[1].trim());
            } else if (tempArr.length == 1) {
                map.put(tempArr[0].trim(), "");
            }
        }
        return map;
    }

    public static void checkNumber(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception ex) {
            throw new RuntimeException("无法转换成Number类型:" + value);
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{5, 6, 7};
        String xx = StringUtil.setIntegerArrToString(a);
        System.out.println(xx);
    }
}



