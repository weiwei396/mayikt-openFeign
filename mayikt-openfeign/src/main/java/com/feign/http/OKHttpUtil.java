package com.feign.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author : weiwei
 * @date : 2021-12-3 10:44
 * @description :OKHttpUtil进行远程调用
 * @modified By :
 * @Version : 1.0.0
 */
public class OKHttpUtil {

    public static OkHttpClient client;

    static {
        client=build();
    }

    public static OkHttpClient build(){
        if(client != null){
            return client;
        }
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }

    public static String get(String url) {
        return get(url, null, new HashMap<String, Object>());
    }

    public static String get(String url,  Map<String, Object> params) {
        return get(url, null, params);
    }

    public static String get(String url, Map<String,String> header, Map<String,Object> params){

        String result=null;
        String paramsStr = "";
        Set<String> keySet=params.keySet();

        int index=0;
        for (String key : keySet) {
            String param = String.format("%s=%s", key, params.get(key));
            if(index==0){
                paramsStr = "?" + param;
            }else{
                paramsStr = paramsStr + "&" + param;
            }
            index++;
        }

        url=url+paramsStr;
        Request.Builder builder=new Request.Builder();
        if(header !=null){
            builder.headers(Headers.of(header));
        }
        Request request=builder.url(url)
                .get()
                .build();
        Call call=client.newCall(request);

        try{
            Response response=call.execute();
            ResponseBody body=response.body();
            result = body.string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }


    public static  String post(String url,Params params){

        FormBody.Builder builder=new FormBody.Builder();
        Set<String> keyset = params.keySet();
        for (String key : keyset) {
            Object valueObj=params.get(key);
            String value;
            if (valueObj instanceof Integer
                    || valueObj instanceof Long
                    || valueObj instanceof Short
                    || valueObj instanceof Float
                    || valueObj instanceof Double
                    || valueObj instanceof String
                    || valueObj instanceof Boolean
                    || valueObj instanceof Byte) {
                value = valueObj.toString();
            }else{
                value = JSON.toJSONString(valueObj);
            }
            builder.add(key,value);
        }

        RequestBody formBody=builder.build();
        Request request=new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        String result=null;
        Call call = client.newCall(request);

        try{
            Response response=call.execute();
            ResponseBody body = response.body();
            result = body.string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }



    public static String postJson(String url,Params params){

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),params.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        String result = null;
        Call call=client.newCall(request);
        try{
            Response response=call.execute();
            ResponseBody body=response.body();
            result = body.string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }


}
