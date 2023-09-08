package org.example.redis.config;

import org.checkerframework.checker.units.qual.C;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/8 17:22
 **/
@Configuration
public class RedissonConfig {

    @Bean
    public Redisson redisson(){
        //此为单机模式
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}
