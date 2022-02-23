package com.feign.http;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : weiwei
 * @date : 2021-12-3 10:41
 * @description :
 * @modified By :
 * @Version : 1.0.0
 */
public class Params extends HashMap<String,Object> {

    public static Params instance(){
        Params params=new Params();
        return params;
    }

    public static Params build(Object obj){
        String jsonStirng= JSON.toJSONString(obj);
        Map<String,String> map=JSON.toJavaObject(JSON.parseObject(jsonStirng),Map.class);
        Params params=Params.instance();
        params.putAll(map);
        return params;
    }

    public Params setItem(String key, String value) {
        super.put(key, value);
        return this;
    }

    public Object getItem(String key) {
        return super.get(key);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
