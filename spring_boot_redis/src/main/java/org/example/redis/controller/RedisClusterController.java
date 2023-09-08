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
 * @Date 2023/9/6 20:56
 **/
@Api(tags = "RedisCluster集群")
@RestController
@RequestMapping("/redisCluster")
public class RedisClusterController {
    private static final Logger logger = LoggerFactory.getLogger(SentinelController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("RedisCluster集群测试")
    @GetMapping("/testCluster")
    public void testCluster(){
        redisTemplate.opsForValue().set("zhuge", "666");
        logger.info(redisTemplate.opsForValue().get("zhuge"));
    }
}
