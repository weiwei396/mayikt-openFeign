package com.feign.annotation;

import com.feign.Processor.HttpRequestRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author : weiwei
 * @date : 2021-12-3 10:20
 * @description : 开启feign
 * @modified By :
 * @Version : 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HttpRequestRegister.class)
public @interface EnabledHttpRequest {
    //扫描路劲
    String [] basePackage();
}
