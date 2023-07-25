package com.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/24 13:25
 **/
@ComponentScan
//@PropertySource 使得某个properties文件中的参数添加到运行时环境中
//@PropertySource("classpath:spring.properties")
public class AppConfig {

}
