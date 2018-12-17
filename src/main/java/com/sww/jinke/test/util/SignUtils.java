package com.sww.jinke.test.util;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: Sun Weiwen
 * @Description: http请求 签名工具类
 * @Date: 13:53 2018/11/12
 */
public class SignUtils {

    public static String signUp(HashMap<String, Object> params) throws Exception{
        if (null == params || params.isEmpty()) {
            return null;
        }
        LinkedHashMap<String, Object> map = sortMapByKey(params);
        StringBuffer stringBuffer = new StringBuffer();
        //进行签名组装
        map.forEach((key, value) -> {
            //若value为空，则不加入签名
            if (null != value) {
                stringBuffer.append(key + "=" + value + "&");
            }
        });
        //将最后一位的 & 去掉
        String signString = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        //将排序的签名信息用MD5加密后 再转换为大写输出
        return EncoderByMd5(signString).toUpperCase();
    }

    /**
     * 根据map的key进行排序
     * @param map
     * @param <K> key
     * @param <V> value
     * @return
     */
    private static <K extends Comparable<? super K>, V> LinkedHashMap<K,V> sortMapByKey(Map<K, V> map) {
        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 利用MD5进行加密
     * @param str  待加密的字符串
     * @return     加密后的字符串
     * @throws NoSuchAlgorithmException   没有这种产生消息摘要的算法
     */
    private static String EncoderByMd5(String str) throws NoSuchAlgorithmException {
        //生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        //计算MD5函数
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    /**
     * 封装请求参数
     * @return HashMap
     * @throws Exception
     */
    public static HashMap<String, Object> param() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid",PropertyUtil.getProperty("jinke.appid"));
        map.put("mchid", PropertyUtil.getProperty("jinke.mchid"));
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("noise",StringUtil.getRandomString(5));
        map.put("appsecret",PropertyUtil.getProperty("jinke.appsecret"));
        String sign = signUp(map);
        map.put("sign", sign);
        map.remove("appsecret");
        return map;
    }

    public  static void main(String[] args) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", PropertyUtil.getProperty("jinke.appid"));
        map.put("mchid", PropertyUtil.getProperty("jinke.mchid"));
        map.put("timestamp", System.currentTimeMillis());
        map.put("noise",StringUtil.getRandomString(5));
        map.put("appsecret",PropertyUtil.getProperty("jinke.appsecret"));
        map.put("projectId", "1ff05e6b-f7b7-4705-be3b-a5118279b406");
        String sign = signUp(map);
        System.out.println("当前时间：" + System.currentTimeMillis());
        System.out.println("加密后的签名：" + sign);
        map.put("sign", sign);
        map.remove("appsecret");
        System.out.println(map);
        String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.getbuildlist");
        ResultMapUtil result = HttpUtils.URLPost(url, map);
        System.out.println(result);
    }

}
