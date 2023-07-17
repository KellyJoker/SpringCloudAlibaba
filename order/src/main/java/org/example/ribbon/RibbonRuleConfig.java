package org.example.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 修改默认的负载均衡策略
 * 注意⚠️：自定义的配置类必须是@Configuration配置类，并且不能写在@SpringbootApplication注解的@ComponentScan扫描得到的地方，
 * 否则自定义的配置类就会被所有的 RibbonClients 共享。
 * @Author danxiaodong
 * @Date 2023/7/17 10:01
 **/
@Configuration
public class RibbonRuleConfig {
    /**
     * 方法名一定要叫 iRule
     * @return IRule
     */
    @Bean
    public IRule iRule() {
        // 随机负载均衡策略
        IRule randomRule = new RandomRule();

        // 轮询负载均衡策略
        IRule roundRobinRule= new RoundRobinRule();

        // 在轮询的基础上进行重试
        IRule retryRule = new RetryRule();

        // 权重策略
        IRule weightedResponseTimeRule = new WeightedResponseTimeRule();

        // 指定使用Nacos提供的负载均衡策略（优先调用同一集群的实例，基于随机权重）
        IRule nacosRule = new NacosRule();

        return randomRule;
    }
}
