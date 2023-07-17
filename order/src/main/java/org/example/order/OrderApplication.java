package org.example.order;

import org.example.ribbon.RibbonRuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
/*//注意⚠️：客户端负载均衡配置位于服务的消费方
@RibbonClients(value = {
        // name 是指定为哪个服务提供方进行负载均衡策略，configuration 指定配置类。
        @RibbonClient(name = "stock-server", configuration = RibbonRuleConfig.class)
        // 可以指定多个 @RibbonClient
})*/
@EnableFeignClients //⚠️若使用feign调用，则需要调用端在启动类上添加 @EnableFeignClients 注解
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class);
    }

    @Bean
    @LoadBalanced //开启负载均衡注解
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
