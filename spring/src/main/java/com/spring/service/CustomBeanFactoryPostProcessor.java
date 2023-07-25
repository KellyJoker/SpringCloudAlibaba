package com.spring.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Description BeanFactoryPostProcessor表示Bean工厂的后置处理器，其实和BeanPostProcessor类似，
 * BeanPostProcessor是干涉Bean的创建过程，BeanFactoryPostProcessor是干涉BeanFactory的创建过程。
 * @Author danxiaodong
 * @Date 2023/7/25 17:15
 **/
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    /**
     * 我们可以在postProcessBeanFactory()方法中对BeanFactory进行加工
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("加工BeanFactory。。。");
    }
}
