package com.spring.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Description 这里我们可以自定义一个bean，这个bean不会经历一个完整的spring生命周期
 * ⚠️ 在通过getBean() 获取bean时，beanName是class类名首字母小写
 * @Author danxiaodong
 * @Date 2023/7/25 17:17
 **/
@Component
public class CustomFactoryBean implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return new UserService();
    }

    @Override
    public Class<?> getObjectType() {
        return UserService.class;
    }
}
