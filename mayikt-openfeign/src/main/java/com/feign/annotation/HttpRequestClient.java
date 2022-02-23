package com.feign.annotation;

import java.lang.annotation.*;

/**
 * @author : weiwei
 * @date : 2021-12-3 10:25
 * @description :接口注解,代理层主要代理接口
 * @modified By :
 * @Version : 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface HttpRequestClient {

    /**
     * 名称
     */
    String name();

    /**
     * url
     */
    String baseUrl();

    /**
     * 前缀
     */
    String prefix() default "";

}
