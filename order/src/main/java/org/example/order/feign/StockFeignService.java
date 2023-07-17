package org.example.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description 添加feign接口和方法
 *  name 指定调用rest接口所对应的服务名
 *  path 指定调用rest接口所在的StockController指定的@RequestMapping
 *  温馨提示：接口方法名可以不与调用接口所在的controller类的方法名一致，但是指定的@RequestMapping必须一致
 * @Author danxiaodong
 * @Date 2023/7/17 14:01
 **/
@FeignClient(value = "stock-server", path = "/stock")
public interface StockFeignService {
    /**
     * 普通stock接口
     * @return String
     */
    @RequestMapping("/stockGeneralCall")
    String stockGeneralCall();

    /**
     * ribbon负载均衡
     * @return String
     */
    @RequestMapping("/stockRibbonCall")
    String stockRibbonCall();

    /**
     * feign调用
     * @return String
     */
    @RequestMapping("/stockFeignCall")
    String stockFeignCall();
}
