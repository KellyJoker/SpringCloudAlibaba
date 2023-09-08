package org.example.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description redis实现分布式锁🔒
 * @Author danxiaodong
 * @Date 2023/9/7 12:42
 **/
@Api(tags = "redis分布式锁实践")
@RestController
@RequestMapping("/redisson")
public class RedissonController {
    @Autowired
    private Redisson redisson;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SentinelController.class);

    @ApiOperation("分布式扣减库存--未加分布式锁")
    @GetMapping("/deductStock")
    public String deductStock(){
        synchronized (this){
            int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock"));
            if (stock > 0){
                int realStock = stock - 1;
                redisTemplate.opsForValue().set("stock", realStock + "");
                logger.info("-----扣减成功！剩余库存：{}-----", realStock);
            }else {
                logger.info("-----扣减失败！库存不足！-----");
            }
        }
        return "end";
    }

    @ApiOperation("分布式扣减库存--添加分布式锁--简单模式")
    @GetMapping("/deductStockAddLockSimple")
    public String deductStockAddLockSimple(){
        String lockKey = "lock:product-1001";
        //加锁
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, "stockkey");
        if (!flag){
            return "error";
        }
        int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock"));
        if (stock > 0){
            int realStock = stock - 1;
            redisTemplate.opsForValue().set("stock", realStock + "");
            logger.info("-----扣减成功！剩余库存：{}-----", realStock);
        }else {
            logger.info("-----扣减失败！库存不足！-----");
        }
        //解锁
        redisTemplate.delete(lockKey);
        return "end";
    }

    @ApiOperation("分布式扣减库存--添加分布式锁--redisson")
    @GetMapping("/deductStockAddLock")
    public String deductStockAddLock(){
        String lockKey = "lock:product-1001";
        //获取锁对象
        RLock redissonLock = redisson.getLock(lockKey);
        //添加分布式锁
        redissonLock.lock();

        try {
            int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock"));
            if (stock > 0){
                int realStock = stock - 1;
                redisTemplate.opsForValue().set("stock", realStock + "");
                logger.info("-----扣减成功！剩余库存：{}-----", realStock);
            }else {
                logger.info("-----扣减失败！库存不足！-----");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        } finally {
            //解锁
            redissonLock.unlock();
        }
        return "end";
    }
}
