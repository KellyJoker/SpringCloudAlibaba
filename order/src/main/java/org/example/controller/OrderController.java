package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    RestTemplate restTemplate;

    /**
     * 普通接口调用，通过RestTemplate调用
     * @return string
     */
    @RequestMapping("/orderGeneralCall")
    public String orderGeneralCall(){
        String stockMsg = restTemplate.getForObject("http://localhost:8082/stock/stockGeneralCall", String.class);
        return "普通订单order接口调用--" + stockMsg;
    }
}
