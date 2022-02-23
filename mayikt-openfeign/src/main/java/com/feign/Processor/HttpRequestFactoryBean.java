package com.feign.Processor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;

/**
 * @author : weiwei
 * @date : 2021-12-3 10:27
 * @description :统一代理类工厂
 * @modified By :FactoryBean 增强实例化之后的bean对象
 * @Version : 1.0.0
 */
public class HttpRequestFactoryBean implements FactoryBean<Object>, InitializingBean {

    //接口类
    private Class<?> clazz;

    private String name;

    private String baseUrl;

    private String prefix;

    public HttpRequestFactoryBean(Class<?> clazz, String name, String baseUrl, String prefix) {
        this.clazz = clazz;
        this.name = name;
        this.baseUrl = baseUrl;
        this.prefix = prefix;
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(this.clazz.getClassLoader(),new Class[]{this.clazz},new HttpRequestProxy(baseUrl,prefix));
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.clazz, "this clazz not null");
        Assert.notNull(this.name, "this name not null");
        Assert.notNull(this.baseUrl, "this baseUrl not null");
        Assert.notNull(this.prefix, "this prefix not null");
    }
}
