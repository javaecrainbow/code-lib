package com.salk.lib.spring;

import java.lang.reflect.Constructor;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p/>
 * Description
 * <p/>
 *
 * @author salkli
 * @date 2022/2/23
 */
@Component
public class PersonBean
    implements DisposableBean, CommandLineRunner, InitializingBean, InstantiationAwareBeanPostProcessor,
    BeanFactoryPostProcessor, BeanNameAware {

    private String age;

    public String getAge() {
        return age;
    }

    @PostConstruct
    public void init() {
        System.out.println("[post construct]");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("disposable bean========");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("commandline runner========" + args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterproperties set========");
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInstantiation========" + bean + " beanName=" + beanName);
        return false;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization========" + bean + " beanName=" + beanName);
        return bean;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
        throws BeansException {
        System.out.println("postProcessProperties========" + bean + " pvs=" + pvs + " beanName=" + beanName);
        return pvs;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
        throws BeansException {
        System.out.println("postProcessBeanFactory=======" + configurableListableBeanFactory);
    }



    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInstantiation========" + bean + " beanName=" + beanName);
        return bean;
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("setBeanName====" + s);
    }
}
