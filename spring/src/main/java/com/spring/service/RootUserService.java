package com.spring.service;

import com.spring.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/24 23:28
 **/
@Component
public class RootUserService {

    /**
     *  spring在解析属性@Value 的值是一个字符串， 属性的类型又是一个User，spring就知道需要将字符串转化为User对象。而spring自身又没有
     *  提供这个转化器，此时会去看用户有没有提供将字符串转为User对象的转化器，若有，则执行该转化器的逻辑，将字符串转为User对象。
     */
    @Value("tom,15")
    private User user;

    public void test(){
        System.out.println(user.getUsername()+"---"+user.getAge());
    }
}
