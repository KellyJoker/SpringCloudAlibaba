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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    /**
     * redis 分布式锁是利用 redis 的 setnx 命令来实现的
     * 格式：setnx key value
     * 将 key 的值设置为 value，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 setnx 不做任何操作。
     * setnx 是【set if not exists（如果不存在，则 set）】简写
     *
     * 当多个请求进来时，会先使用setnx命令存入一个值，若设置成功，返回true，表示加锁成功；
     * 若值已存在，redis不会做任何操作，则会 设置不成功，返回false，表示获取锁失败，需要等待锁释放；
     * 当拿到锁的线程执行完成同步代码块后，再将key删除，后续线程再执行setnx命令时，就会加锁成功！
     * @return String
     */
    @ApiOperation("分布式扣减库存--添加分布式锁--简单模式")
    @GetMapping("/deductStockAddLockSimple")
    public String deductStockAddLockSimple(){
        /**
         * 在实际的业务实现中，例如修改某一个商品库存的操作，可以将该商品id作为redis分布式锁的键值（唯一）进行加锁，
         * 这样多个线程进行操作时就能保证该商品库存操作的安全。
         */
        String lockKey = "lock:product-1001";

        /**
         * 加锁线程的🆔，保持唯一
         */
        String clientId = UUID.randomUUID().toString();

        /**
         * 返回true，表示redis之前不存在这个key， 这条命令就是设置成功了，相当于拿锁成功；
         * 返回false，表示redis已经存在了这个key，这条命令无任何操作，相当于拿锁失败。
         */
        //加锁
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId);

        /**
         * 思考一个问题：
         *      在加锁和设置超时时间过程中，也有可能发生异常，从而导致超时时间没有设置成功。
         *      虽然这个概率很小，但是在高并发情况下依旧可能会发生。那该如何避免呢？
         * 解决方法：
         *      若想解决这个问题，需保证redis加锁和设置超时时间是一个原子操作。
         *      这里可以使用 redisTemplate.opsForValue().setIfAbsent()方法来同时设置key和超时时间；
         *      也以使用redisson来保证该操作是原子操作。
         */
        logger.error("-----若发生异常-----");
        //原子命令，设置key和超时时间会一起执行，不会被打断
        //redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);

        /**
         * 在执行具体的业务逻辑时，虽然代码块被try{}catch{}finally{}包裹，解锁逻辑就一定会执行成功么？
         * 答案并不是。如果一个线程获取锁后，在执行业务代码时服务器宕机，后续服务器重启正常后，解锁逻辑还是不会执行，
         * 这样就会导致后续线程一直获取不到锁，从而导致死锁。
         * 解决方法：给key设置一个超时时间即可。若真的服务器宕机，超过时间锁就会被释放，后续线程就会成功获取锁，执行线程。
         */
        //设置超时时间
        redisTemplate.expire(lockKey, 10, TimeUnit.SECONDS);
        /**
         * 锁超时时间问题的思考：
         *      在实际执行业务逻辑的过程中，业务逻辑有时需要花费比较长的时间来执行，而此时设置的超时时间又太短，
         *      导致业务逻辑还未执行完毕就释放锁，从而导致一系列问题。那如何可以确保业务逻辑的正常执行完毕呢？
         * 解决方法：
         *      锁续命机制：（定时任务为锁延长超时时间）
         *          会有一个线程监视主线程，每隔一段时间去查看主线程是否还持有锁，如果持有，则延长锁的时间，以确保线程可以正常执行完毕。
         *          其他线程会尝试能否加锁成功，若不能，则会while循环，间歇性尝试加锁。（阻塞期间不消耗性能）
         *      redisson分布式锁在加锁逻辑中就实现了锁续命机制。
         */


        if (!flag){
            return "error";
        }

        //------------------------------业务逻辑------------------------------
        /**
         * 在执行正常的业务逻辑中，可能会抛出各种异常会导致后面的解锁逻辑不会执行，从而导致死锁。
         * 若要保证解锁逻辑一定会执行，则需要将业务代码try{}catch{}finally{}包裹，
         * finally代码块中执行解锁逻辑
         */
        try{
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
        }finally {
            /**
             * 思考一个问题：
             *      这里在解锁时，若直接解锁，我自己加的锁可能会被别的线程给释放了。
             * 解决方法：
             *      可以给每个线程设置一个唯一的id，若不是自己的线程，不给解锁。
             *      clientId.equals(redisTemplate.opsForValue().get(lockKey))
             */
            //解锁--优化前
            redisTemplate.delete(lockKey);

            /**
             * 此时优化后的代码依旧存在一个问题：
             *      clientId.equals(redisTemplate.opsForValue().get(lockKey))和redisTemplate.delete(lockKey)不是原子操作，
             *      这期间仍然有安全问题。
             * 解决方法：
             *      redisTemplate没有原生的方法可以实现这个操作，可以使用redisson分布式锁来实现
             */
            //解锁--优化后
//            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))){
//                redisTemplate.delete(lockKey);
//            }
        }
        //------------------------------业务逻辑------------------------------

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
