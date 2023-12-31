package org.example.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/6 14:20
 **/
@SpringBootApplication
@MapperScan("org.example.redis.mapper")
public class RedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class);
    }
}
