package org.example.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.example.order.beans.User;
import org.example.order.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api(tags = "订单测试接口")
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
    @ApiOperation("普通接口调用，通过RestTemplate调用")
    @GetMapping("/orderGeneralCall")
    public String orderGeneralCall(){
        String stockMsg = restTemplate.getForObject("http://localhost:8082/stock/stockGeneralCall", String.class);
        return "普通订单order接口调用--" + stockMsg;
    }

    /**
     * 使用nacos后，用RestTemplate进行服务调用，可以使用微服务名称 （spring.application.name）调用，
     * 无须使用IP地址和端口调用
     * @return string
     */
    @ApiOperation("nacos服务名调用")
    @GetMapping("/orderNacosGeneralCall")
    public String orderNacosGeneralCall(){
        String stockMsg = restTemplate.getForObject("http://stock-server/stock/stockGeneralCall", String.class);
        return "nacos服务订单order接口调用--" + stockMsg;
    }

    /**
     * 使用ribbon代码配置负载均衡策略
     * @return String
     */
    @ApiOperation("ribbon代码配置负载均衡策略")
    @GetMapping("/orderRibbonCall")
    public String orderRibbonCall(){
        String stockMsg = restTemplate.getForObject("http://stock-server/stock/stockRibbonCall", String.class);
        return "ribbon订单order接口调用--" + stockMsg;
    }

    /**
     * 使用feign优雅的调用外部接口
     * @return String
     */
    @ApiOperation("使用feign优雅的调用外部接口")
    @GetMapping("/orderFeignCall")
    public String orderFeignCall(){
        String feignMsg = stockFeignService.stockFeignCall();
        return "feign订单order接口调用--" + feignMsg;
    }

    @ApiOperation("测试接口")
    @ApiImplicitParam(name = "id", value = "用户id", defaultValue = "1", required = true)
    @GetMapping("/{id}")
    public String qryById(@PathVariable Integer id){
        return "hi--" + id;
    }

    @ApiOperation("用户添加")
    @PostMapping("/add")
    public String addUser(@RequestBody User user){
        return user.toString();
    }
}
