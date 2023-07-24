package com.xiaodong.service;

import com.spring.BeanPostProcessor;
import com.spring.annotation.Component;

import java.lang.reflect.Field;

@Component
public class CustomValueBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(GetValue.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean, field.getAnnotation(GetValue.class).value());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        // bean
        return bean;
    }
}
