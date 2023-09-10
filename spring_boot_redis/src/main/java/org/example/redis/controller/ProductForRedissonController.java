package org.example.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.redis.entity.ProductInfo;
import org.example.redis.service.IProductService;
import org.redisson.Redisson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 高并发环境下商品库存操作
 * @Author danxiaodong
 * @Date 2023/9/9 16:57
 **/
@Api(tags = "高并发环境下商品库存操作")
@RestController
@RequestMapping("/product")
public class ProductForRedissonController {
    private static final Logger logger = LoggerFactory.getLogger(ProductForRedissonController.class);

    @Autowired
    private IProductService productService;

    @ApiOperation("商品信息添加")
    @PostMapping("/add")
    public String add(@RequestBody ProductInfo productInfo){
        return productService.create(productInfo);
    }

    @ApiOperation("商品信息修改")
    @PostMapping("/update")
    public String update(@RequestBody ProductInfo productInfo){
        return productService.update(productInfo);
    }

    @ApiOperation("商品信息查询")
    @PostMapping("/queryProductInfo")
    public ProductInfo queryProductInfo(String productId){
        return productService.getProductInfo(Long.valueOf(productId));
    }
}
