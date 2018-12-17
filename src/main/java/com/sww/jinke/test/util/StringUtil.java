package com.sww.jinke.test.util;

import java.util.*;

public class StringUtil {

    /**
     * 生成一个32位随机字符串id
     * @return String
     */
    public static String UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成一个第一位不为0的6位随机码
     * @return
     */
    public static String RandNumberSex() {
        String str;
        int x = (int) (((Math.random())+1)*100000);
        str = String.valueOf(x);
        return str;
    }

    /**
     * 生成一个可控长度的随机字符串
     * @param length 字符长度
     * @return
     */
    public static String getRandomString(int length) {
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    /**
     * 校验字符是否为空
     * @param value
     * @return
     */
    public static boolean isNullOrEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.RandNumberSex());
    }
}
