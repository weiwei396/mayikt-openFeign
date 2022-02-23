package com.feign.Processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @author : weiwei
 * @date : 2021-12-2 16:03
 * @description :
 * @modified By :
 * @Version : 1.0.0
 */
//@Configuration
//public class BeanDefinitionRegister implements BeanDefinitionRegistryPostProcessor {
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
//        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
//
//        //根据bean的名字生成规则
//        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
//
//        //设置需要扫描的注解
//        scanner.addIncludeFilter(new AnnotationTypeFilter(EnableOpenfegin.class));
//        //需要扫描的路劲
//        scanner.scan("com.feign");
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//
//    }
//}
