package com.sww.jinke.test.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



/**
 * @Author: Sun Weiwen
 * @Description:  Http连接工具类
 * @Date: 16:23 2018/11/9
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    //请求链接中用来拼接参数的特殊的字符，定义成常量，方便以后修改
    private static final String URL_PARAM_CONNECT_FLAG = "&";

    private static final String EMPTY = "";


    //声明一个多线程安全连接管理类变量
    private static MultiThreadedHttpConnectionManager connectionManager = null;

    //将参数提取成变量，以便日后修改

    private static int connectionTimeout = 25000;

    private static int socketTimeout = 25000;

    private static int maxConnectionPerHost = 20;

    private static int maxTotalConnections = 20;

    //声明client变量
    private static HttpClient httpClient;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeout);
        connectionManager.getParams().setSoTimeout(socketTimeout);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        httpClient = new HttpClient(connectionManager);
    }

    /**
     * post 方式提交数据
     *
     * @param url      待请求的URL
     * @param params   要提交的数据
     * @return         响应结果
     * @throws IOException IO异常
     */
    public static ResultMapUtil URLPost(String url, HashMap<String, Object> params){
        //创建一个Map集合，用于存储返回结果集
        ResultMapUtil result = new ResultMapUtil();
        String response = EMPTY;
        //创建一个请求方式对象
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            //postMethod.setRequestHeader("content-Type","application/x-www-form-urlencoded;charset=" + enc);
            //将请求参数放人postMethod中
            if (params!=null){
                Set<String> keySet = params.keySet();
                for (String key : keySet){
                    Object value = params.get(key);
                    postMethod.addParameter(key, value.toString());
                }
            }
            //执行postMethod
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK){                  //HttpStatus.SC_OK就是200，服务器正确处理了请求
                response = postMethod.getResponseBodyAsString();  //返回字符串格式的结果
                //封装状态信息和结果
                logger.info(JSON.toJSON(response).toString());
                result.put("status",200);
                result.put("data",response);
            }else {
                result.put("status",postMethod.getStatusCode());
                result.put("data",null);
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("发生网络异常", e);
            e.printStackTrace();
        }finally {
            if (postMethod != null){
                //手动释放掉对象
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return result;
    }

    /**
     * 这个方法与上面相同，只不过请求参数是json格式的
     * @param url
     * @param json
     * @param enc
     * @return
     */

    public static ResultMapUtil URLPost(String url, String json, String enc){

        ResultMapUtil result = new ResultMapUtil();
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            RequestEntity se = new StringRequestEntity(json, "application/json", "UTF-8");
            postMethod.setRequestEntity(se);
            //执行postMethod
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
                result.put("status",200);
                result.put("data",response);
            } else {
                result.put("status",postMethod.getStatusCode());
                result.put("data",null);
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return result;
    }

    public static String URLGet(String url, Map<String, String> params, String enc) {

        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(EMPTY);

        if (strtTotalURL.indexOf("?") == -1) {          //返回结果是-1代表没有找到该字符串，那么就在url后面拼接一个问号
            strtTotalURL.append(url).append("?").append(getUrl(params, enc));
        } else {
            strtTotalURL.append(url).append("&").append(getUrl(params, enc));
        }

        try {
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //执行getMethod
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = getMethod.getResponseBodyAsString();
            } else {
                response=getMethod.getResponseBodyAsString();
            }
        } catch (HttpException e) {
            logger.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * 根据Map生成URL字符串
     * @param map         Map
     * @param valueEnc    URL编码
     * @return            URL
     */
    private static String getUrl(Map<String, String> map, String valueEnc) {
        if (null ==map || map.keySet().size() == 0){
            return (EMPTY);
        }
        StringBuffer url = new StringBuffer();
        Set<String> keys = map.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (map.containsKey(key)) {
                String val = map.get(key);
                String str = val != null ? val : EMPTY;
                try {
                    str = URLEncoder.encode(str, valueEnc);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
            }
        }
        String strURL = EMPTY;
        strURL = url.toString();
        //为了保证 "&" 符号后面一定有参数，如果"&" 后面没有东西，就把"&"去掉
        if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1))) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }
        return (strURL);
    }


}
