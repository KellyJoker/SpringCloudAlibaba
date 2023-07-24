package com.xiaodong.service;

import com.spring.annotation.Autowired;
import com.spring.BeanNameAware;
import com.spring.annotation.Component;
import com.spring.InitializingBean;

@Component
public class UserService implements UserInterface, BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    @GetValue("xxx")
    private String test;


    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void test() {
        System.out.println(beanName);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化方法开始啦。。。");
    }
}
