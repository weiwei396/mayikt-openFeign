package com.feign.Processor;

import com.feign.annotation.EnabledHttpRequest;
import com.feign.annotation.HttpRequest;
import com.feign.annotation.HttpRequestClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author : weiwei
 * @date : 2021-12-3 13:37
 * @description :
 * @modified By :
 * @Version : 1.0.0
 */
public class HttpRequestRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private Environment environment;
    private ResourceLoader resourceLoader;
    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader=resourceLoader;
    }

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        ClassPathScanningCandidateComponentProvider scanner=getScanner();
        scanner.setResourceLoader(this.resourceLoader);


        //获取相关扫描包路劲
        Set<String> basePackages = getBasePackages(annotationMetadata);

        AnnotationTypeFilter annotationTypeFilter=new AnnotationTypeFilter(HttpRequestClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        final Set<String> clientClasses=new HashSet<>();

        AbstractClassTestingTypeFilter filter = new AbstractClassTestingTypeFilter() {

            @Override
            protected boolean match(ClassMetadata classMetadata) {
                String cleaned=annotationMetadata.getClassName().replace("\\$", ".");
                return clientClasses.contains(cleaned);
            }
        };
        scanner.addIncludeFilter(new HttpRequestRegister.AllTypeFilter(Arrays.asList(filter,annotationTypeFilter)));

        for (String basePackage : basePackages) {

            Set<BeanDefinition> candidateComponents=scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if(candidateComponent instanceof AnnotatedBeanDefinition){

                    AnnotatedBeanDefinition beanDefinition= (AnnotatedBeanDefinition)candidateComponent;
                    AnnotationMetadata metadata=beanDefinition.getMetadata();

                    Map<String,Object> attributes = metadata.getAnnotationAttributes(HttpRequestClient.class.getCanonicalName());
                    String ClassName=candidateComponent.getBeanClassName();
                    String name=(String) attributes.get("name");
                    String baseUrl = (String) attributes.get("baseUrl");
                    String prefix = (String) attributes.get("prefix");

                    //bean工厂获取相应的bean对象

                    BeanDefinitionBuilder definition=BeanDefinitionBuilder.genericBeanDefinition(HttpRequestFactoryBean.class);
                    definition.addConstructorArgValue(Class.forName(ClassName));
                    definition.addConstructorArgValue(name);
                    definition.addConstructorArgValue(baseUrl);
                    definition.addConstructorArgValue(prefix);

                    AbstractBeanDefinition handlerDefinition = definition.getBeanDefinition();

                    //注册
                    String registerBeanName = name;

                    //向Spring的上下文注册bean组件
                    BeanDefinitionHolder holder = new BeanDefinitionHolder(handlerDefinition,registerBeanName,new String[]{registerBeanName});
                    BeanDefinitionReaderUtils.registerBeanDefinition(holder,beanDefinitionRegistry);
                }
            }
        }

    }



    protected  ClassPathScanningCandidateComponentProvider getScanner(){

        return new ClassPathScanningCandidateComponentProvider(false,this.environment){
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)  {

                boolean isCandidate = false;
                if(beanDefinition.getMetadata().isIndependent()){
                    if(!beanDefinition.getMetadata().isAnnotation()){
                        isCandidate=true;
                    }
                }
                return isCandidate;
            }
        };
    }


    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata){

        Map<String,Object> attributes=importingClassMetadata
                .getAnnotationAttributes(EnabledHttpRequest.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String packages : (String[]) attributes.get("basePackage")) {
            if(StringUtils.hasText(packages)){
                basePackages.add(packages);
            }
        }

        if(basePackages.isEmpty()){
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }


    private static class AllTypeFilter implements TypeFilter{

        private final List<TypeFilter> delegates;

        private AllTypeFilter(List<TypeFilter> delegates) {
            Assert.notNull(delegates, "This argument is required, it must not be null");
            this.delegates = delegates;
        }


        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {

            for (TypeFilter delegate : this.delegates) {
                if(!delegate.match(metadataReader,metadataReaderFactory)){
                    return false;
                }
            }
            return true;
        }
    }

}
