package org.example.order.controller;

import org.example.order.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    StockFeignService stockFeignService;

    /**
     * 普通接口调用，通过RestTemplate调用
     * @return string
     */
    @RequestMapping("/orderGeneralCall")
    public String orderGeneralCall(){
        String stockMsg = restTemplate.getForObject("http://localhost:8082/stock/stockGeneralCall", String.class);
        return "普通订单order接口调用--" + stockMsg;
    }

    /**
     * 使用nacos后，用RestTemplate进行服务调用，可以使用微服务名称 （spring.application.name）调用，
     * 无须使用IP地址和端口调用
     * @return string
     */
    @RequestMapping("/orderNacosGeneralCall")
    public String orderNacosGeneralCall(){
        String stockMsg = restTemplate.getForObject("http://stock-server/stock/stockGeneralCall", String.class);
        return "nacos服务订单order接口调用--" + stockMsg;
    }

    /**
     * 使用ribbon代码配置负载均衡策略
     * @return String
     */
    @RequestMapping("/orderRibbonCall")
    public String orderRibbonCall(){
        String stockMsg = restTemplate.getForObject("http://stock-server/stock/stockRibbonCall", String.class);
        return "ribbon订单order接口调用--" + stockMsg;
    }

    /**
     * 使用feign优雅的调用外部接口
     * @return String
     */
    @RequestMapping("/orderFeignCall")
    public String orderFeignCall(){
        String feignMsg = stockFeignService.stockFeignCall();
        return "feign订单order接口调用--" + feignMsg;
    }
}