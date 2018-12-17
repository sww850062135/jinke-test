package com.sww.jinke.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static  final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_CHINESE = "yyyy年M月d日";


    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentDateTime() {
        String datestr;
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        Date date =new Date();
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        //calendar.add(Calendar.DAY_OF_MONTH,-17);
        //datestr = df.format(calendar.getTime());
        datestr = df.format(date);
        return datestr;
    }
    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowTime(String type) {
        return new SimpleDateFormat(type).format(new Date());
    }

    public static String getDateTime(Date date) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
    }

    /**
     * 根据当前时间加减获得时间
     * @param timeUnit 时间单位
     * @param time 时间
     * @return
     */
    public static  String  getNow_Pre_Date(int timeUnit,int time){
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, time);
        return getDateTime(calendar.getTime());
    }
    public static void main(String[] args) {
        System.out.println(DateUtils.getCurrentDateTime());
    }

    /**
     * 使用预设格式提取字符串日期
     * @param strDate  日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, DATE_TIME_FORMAT);
    }

    /**
     * 使用用户格式提取字符串日期
     * @param strDate           日期字符串
     * @param dateTimeFormat    日期格式
     * @return
     */
    private static Date parse(String strDate, String dateTimeFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateTimeFormat);
        try {
            return df.parse(strDate);
        }catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
