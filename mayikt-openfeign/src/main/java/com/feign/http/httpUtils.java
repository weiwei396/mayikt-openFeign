package com.feign.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author : weiwei
 * @date : 2021-12-1 17:04
 * @description :
 * @modified By :
 * @Version : 1.0.0
 */
public class httpUtils {

    private static RequestConfig requestConfig = null;
    private static final Log log = LogFactory.getLog(httpUtils.class);
    private static final int SOCKET_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 5000;

    static {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
    }

    /**
     * post请求传输json参数
     *
     * @param url       url地址
     * @param jsonParam 参数
     * @return
     */
    public static  JSONObject httpPost(String url, JSONObject jsonParam) {
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            if (null != jsonParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    // 把json字符串转换成json对象
                    jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {
                    log.error("请求服务器端出错：", e);
                }
            }
        } catch (IOException e) {
            log.error("请求服务器端出错：", e);
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }


    /**
     *
     * GET请求传输json参数
     * @param url       url地址
     * @param jsonParam 参数
     * @return
     *
     */
    public static JSONObject httpGet(String url, JSONObject jsonParam){
        JSONObject jsonResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet=null;
        // 设置请求和传输超时时间
        try {
            if(null !=jsonParam){
                url=getMapToString(url,jsonParam);
            }
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse result = httpClient.execute(httpGet);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = "";
                try {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    // 把json字符串转换成json对象
                    jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {
                    log.error("请求服务器端出错：", e);
                }
            }
        } catch (IOException e) {
            log.error("请求服务器端出错：", e);
        } finally {
            httpGet.releaseConnection();
        }
        return jsonResult;
    }

    /**
     *
     * @param jsonObject
     * @descript:jsonObject转成格式化的String
     */
    public static String getMapToString(String url,JSONObject jsonObject){
        Map<String,Object> map = jsonObject.toJavaObject(Map.class);
        StringBuilder str=new StringBuilder(url);
        if(map.size()>0) {
            str.append("?");
            for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
                str.append(stringObjectEntry.getKey() + "=" + stringObjectEntry.getValue() + "&");
            }
        }
        if(str.substring(str.length()-1).equals("&")) {
            str = str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    //test http
    public static void main(String[] args) {
        System.out.println(httpGet("http://localhost:8080/test/sendTestMsg", null));
    }

}
