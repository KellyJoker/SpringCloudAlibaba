package com.spring.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Description BeanPostProcess表示Bena的后置处理器，我们可以定义一个或多个BeanPostProcessor
 * 一个BeanPostProcessor可以在任意一个Bean的初始化之前以及初始化之后去额外的做一些用户自定义的逻辑，
 * 当然，我们可以通过判断beanName来进行针对性处理（针对某个Bean，或某部分Bean）。
 * 我们可以通过定义BeanPostProcessor来干涉Spring创建Bean的过程。
 * @Author danxiaodong
 * @Date 2023/7/25 17:07
 **/
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("rootUserService".equals(beanName))
            System.out.println("rootUserService初始化前。。。");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("rootUserService".equals(beanName))
            System.out.println("rootUserService初始化后。。。");
        return bean;
    }
}
