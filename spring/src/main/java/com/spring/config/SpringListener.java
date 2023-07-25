package com.spring.config;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/24 22:52
 **/
@Configuration
public class SpringListener {
    //事件监听器
    @Bean
    public ApplicationListener applicationListener(){
        return new ApplicationListener() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("接收到事件---" + event.toString());
            }
        };
    }
}
