package org.example.mybatisplus.entity;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/22 10:26
 **/
@Component
public class User implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化方法开始。。。");
    }
}
