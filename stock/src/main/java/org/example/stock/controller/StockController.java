package org.example.stock.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Value("${server.port}")
    String port;

    /**
     * 普通stock接口
     * @return String
     */
    @RequestMapping("/stockGeneralCall")
    public String stockGeneralCall(){
        return "这是一个普通stock接口调用！";
    }

    /**
     * ribbon负载均衡
     * @return String
     */
    @RequestMapping("/stockRibbonCall")
    public String stockRibbonCall(){
        return "ribbon负载均衡--" + port;
    }

    /**
     * feign调用
     * @return String
     */
    @RequestMapping("/stockFeignCall")
    public String stockFeignCall(){
        return "stock接口feign调用！";
    }
}
