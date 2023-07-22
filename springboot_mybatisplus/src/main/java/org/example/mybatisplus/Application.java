package org.example.mybatisplus;

import org.example.mybatisplus.entity.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/21 14:24
 **/
@SpringBootApplication
@MapperScan("org.example.mybatisplus.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
