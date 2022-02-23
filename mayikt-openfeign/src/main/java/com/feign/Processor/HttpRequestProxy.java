package com.feign.Processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feign.annotation.HttpRequest;
import com.feign.http.OKHttpUtil;
import com.feign.http.Params;
import com.feign.http.httpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author : weiwei
 * @date : 2021-12-3 10:30
 * @description :动态代理生成@HttpRequstClient标记接口的代理类
 * @modified By :
 * @Version : 1.0.0
 */
@Slf4j
public class HttpRequestProxy implements InvocationHandler {

    private String baseUrl;
    private String prefix;

    public HttpRequestProxy(String baseUrl, String prefix) {
        this.baseUrl = baseUrl;
        this.prefix = prefix;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpRequest annotation=method.getAnnotation(HttpRequest.class);
        String path=annotation.path();
        boolean annotationJson=annotation.isJson();
        RequestMethod requestMethod=annotation.method();
        Class<?> returnType=method.getReturnType();
        args=getArgs(args);
        String result=null;
        String url=rewriteUrl(path,args);
        switch (requestMethod){
            case GET:{
                result= OKHttpUtil.get(url);
                break;
            }
            case POST:{
                if(annotationJson){
                    result = OKHttpUtil.postJson(url,Params.build(args[0]));
                }else{
                    result = OKHttpUtil.post(url,Params.build(args[0]));
                }
                break;
            }
        }
        log.info("\nrequest [{}] url -> {}\nparams -> {}\nresponse -> {}", requestMethod, url, Params.build(args[0]), result);
        return JSON.parseObject(result,returnType);
    }



    private Object[] getArgs(Object[] args){
        if(args==null || args.length==0){
            args=new Object[]{new Object()};
        }
        return args;
    }

    private String rewriteUrl(String path,Object[] args){
        if(args==null || args.length==0){
            return completeUrl(path);
        }
        Map<String,Object> currentParams= JSON.parseObject(JSON.toJSONString(args[0]),Map.class);
        for (String key : currentParams.keySet()) {
            if(path.contains("?")){
                path=path+"&"+key+"="+currentParams.get(key);
            }else{
                path=path+"?"+key+"="+currentParams.get(key);
            }
        }
        return completeUrl(path);
    }

    private String completeUrl(String path) {
        return baseUrl+prefix+path;
    }


    //获取服务器的存活服务地址和服务名称  Map<String,HashSet<ServiceInstance>> map
    public JSONObject getMapAdress(){
        JSONObject result = httpUtils.httpPost("http://localhost:8848"+ "/mayikt/clientActiveService", null);
        return result;
    }
}
