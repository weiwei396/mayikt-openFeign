package com.feign.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * @author : weiwei
 * @date : 2021-12-3 10:21
 * @description :方法注解标记方法
 * @modified By :
 * @Version : 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpRequest {
    /**
     * 请求路劲
     */
    String path();

    /**
     * 请求方式 ：GET POST
     * @return
     */
    RequestMethod method();

    /**
     * 是否为application/json 传输
     * @return
     */
    boolean isJson()default true;


}

