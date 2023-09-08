package org.example.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/6 14:28
 **/
@Api(tags = "redis哨兵模式测试接口")
@RestController
@RequestMapping("/testSentinel")
public class SentinelController {
    private static final Logger logger = LoggerFactory.getLogger(SentinelController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("redis哨兵模式测试接口")
    @GetMapping("/sentinel")
    public void testSentinel(){
        int i = 1;
        while (true){
            try {
                redisTemplate.opsForValue().set("redis"+i, i+"");
                System.out.println("设置key："+ "redis" + i);
                i++;
                Thread.sleep(1000);
            }catch (Exception e){
                logger.error("错误：", e);
            }
        }
    }
}
