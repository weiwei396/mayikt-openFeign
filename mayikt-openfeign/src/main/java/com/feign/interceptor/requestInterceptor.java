package com.feign.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : weiwei
 * @date : 2021-12-1 17:47
 * @description :jdk动态代理--根据@EnableOpenfegin生成相应的代理类进行处理
 * @modified By :
 * @Version : 1.0.0
 */
public class requestInterceptor implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
