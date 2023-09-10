package org.example.redis.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.redis.entity.ProductInfo;
import org.example.redis.mapper.ProductInfoMapper;
import org.example.redis.service.IProductService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/9 17:03
 **/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductService {
    @Autowired
    private Redisson redisson;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final Integer PRODUCT_CACHE_TIMEOUT = 60 * 60 * 24;

    public static final String EMPTY_CACHE = "{}";
    public static final String PRODUCT_CACHE = "product:cache:";
    public static final String LOCK_PRODUCT_UPDATE_PREFIX = "lock:product:update";
    public static final String LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX = "lock:product:hot_cache_create";

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * 添加商品
     *
     * @param productInfo
     * @return
     */
    @Override
    public String create(ProductInfo productInfo) {
        //读写锁
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(LOCK_PRODUCT_UPDATE_PREFIX + productInfo.getId());
        RLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            baseMapper.insert(productInfo);
            logger.info("-----返回商品id={}-----", productInfo.getId().toString());
            /**
             * redis缓存需设置超时时间，可以更好的缓存热点数据。
             */
            //redisTemplate.opsForValue().set(PRODUCT_CACHE + productInfo.getId(), JSON.toJSONString(productInfo));
            redisTemplate.opsForValue().set(PRODUCT_CACHE + productInfo.getId(), JSON.toJSONString(productInfo),
                    genProductCacheTimeout(), TimeUnit.SECONDS);
        }catch (Exception e){
            logger.info(e.getMessage());
        }finally {
            writeLock.unlock();
        }
        return "success";
    }

    /**
     * 修改商品
     *
     * @param productInfo
     * @return
     */
    @Override
    public String update(ProductInfo productInfo) {
        //读写锁
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(LOCK_PRODUCT_UPDATE_PREFIX + productInfo.getId());
        RLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            baseMapper.updateById(productInfo);
            /**
             * redis缓存需设置超时时间，可以更好的缓存热点数据。
             */
            //redisTemplate.opsForValue().set(PRODUCT_CACHE + productInfo.getId(), JSON.toJSONString(productInfo));
            redisTemplate.opsForValue().set(PRODUCT_CACHE + productInfo.getId(), JSON.toJSONString(productInfo),
                    genProductCacheTimeout(), TimeUnit.SECONDS);
        }catch (Exception e){
            logger.info(e.getMessage());
        }finally {
            writeLock.unlock();
        }
        return "success";
    }

    /**
     * 查询商品
     *
     * @param productId
     * @return
     */
    @Override
    public ProductInfo getProductInfo(Long productId) {
        ProductInfo productInfo = null;
        String productCacheKey = PRODUCT_CACHE + productId;

        //查询缓存
        getProductInfoFromCache(productCacheKey, productInfo);
        if (productInfo != null){
            return productInfo;
        }

        /**
         * 双重检测锁机制：避免突发性热点缓存重建导致的数据库压力增大。
         * 产生的原因：由于某一冷门商品热度暴增，大量的请求访问redis缓存后发现没有数据，会并发去查询数据库，从而导致数据库压力倍增。
         * 原理：当大量请求去查询数据库的时候，此时只会有一个线程去重建缓存，当缓存重建完成后，后续进入到同步代码块的线程就会直接去缓存查询，
         *      大大减小了数据库的压力。
         */
        //添加分布式锁--解决热点缓存并发重建问题
        RLock hotCacheCreateLock = redisson.getLock(LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX + productId);
        hotCacheCreateLock.lock();
        try {
            //查询缓存
            getProductInfoFromCache(productCacheKey, productInfo);
            if (productInfo != null){
                return productInfo;
            }

            //解决数据库、缓存双写不一致进行加锁处理
//            RLock productUpdateLock = redisson.getLock(LOCK_PRODUCT_UPDATE_PREFIX + productId);
//            productUpdateLock.lock();
            //这里可以使用读锁来优化，提升效率 (读操作使用读锁来加锁；写操作使用写锁来加锁！！！注意⚠️读锁和写锁必须是同一把锁，这样写写、读写操作才能互斥)
            RReadWriteLock readWriteLock = redisson.getReadWriteLock(LOCK_PRODUCT_UPDATE_PREFIX + productId);
            RLock readLock = readWriteLock.readLock();
            readLock.lock();
            try {
                /**
                 * 思考这样一个问题：
                 *      突发性热点缓存重建导致系统压力暴增。
                 *      由于某一冷门商品热度暴增，大量的请求访问redis缓存后发现没有数据，会同时去查询数据库，从而导致数据库压力倍增。
                 */
                //查询数据库
                productInfo = baseMapper.selectById(productId);

                /**
                 * 思考这样一个问题：
                 *      在查询数据库和写入缓存之间，可能会有其他异常操作导致数据库缓存双写不一致。
                 * 解决方法：
                 *      加锁
                 */

                if (productInfo!=null){
                    //查询到的数据放入缓存
                    redisTemplate.opsForValue().set(productCacheKey, JSON.toJSONString(productInfo),
                            genProductCacheTimeout(), TimeUnit.SECONDS);
                }else {
                    /**
                     * 思考这样一个场景：
                     *      当前端有大量的请求到后端查询缓存和数据库中都不存在的数据，导致不存在的数据每次请求都要到数据库去查询，会增加数据库的压力。
                     *      这种现象被称为【缓存穿透】。
                     *  造成缓存穿透的基本原因有两个：
                     *      第一， 自身业务代码或者数据出现问题。
                     *      第二， 一些恶意攻击、 爬虫等造成大量空命中。
                     *  解决方法：
                     *      1. 缓存空对象
                     *      2. 布隆过滤器
                     */
                    //这里缓存空对象，防止缓存穿透。过期时间不宜过长，防止黑客利用不存在的数据攻击导致redis存入大量的空缓存，影响服务器性能。
                    redisTemplate.opsForValue().set(productCacheKey, EMPTY_CACHE, genEmptyCacheTimeout(), TimeUnit.SECONDS);
                }
            }catch (Exception e){
                logger.info(e.getMessage());
            }finally {
                //解锁🔓
//                productUpdateLock.unlock();
                readLock.unlock();
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }finally {
            //解锁🔓
            hotCacheCreateLock.unlock();
        }

        return productInfo;
    }

    /**
     * 从缓存获取数据
     * @param productCacheKey
     * @param productInfo
     * @return
     */
    private void getProductInfoFromCache(String productCacheKey, ProductInfo productInfo){
        //先从缓存获取值，若缓存没有，则去数据库查询
        String productStr = redisTemplate.opsForValue().get(productCacheKey);
        if (!StringUtils.isEmpty(productStr)){
            //若缓存为空，直接返回空对象至前端
            if (EMPTY_CACHE.equals(productStr)){
                //空缓存若持续被访问，也需要设置延期，只不过超时时间比较短。
                redisTemplate.expire(productCacheKey, genEmptyCacheTimeout(), TimeUnit.SECONDS);
                productInfo = new ProductInfo();
                return;
            }
            productInfo = JSON.parseObject(productStr, ProductInfo.class);
            /**
             * 读延期，实现了简单的数据冷热分离，即尽可能将热门数据缓存在redis中，冷门数据不会在redis缓存中。
             * 缓存热点数据：每查询一次缓存数据，延长超时时间
             * 优点：热点数据会一直存储在缓存里，减轻了数据库的压力
             */
            redisTemplate.expire(productCacheKey, PRODUCT_CACHE_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    /**
     * 思考这样一个问题：
     *       当某一批商品批量设置的redis缓存在某一刻全部失效，之后大量的请求查询这批商品，导致所有的请求都打到数据库，这样会导致数据库
     *       在这一瞬间承受巨大压力，严重时可能会导致数据库宕机。这样的现象我们称之为【缓存失效（击穿）】。
     * 解决方法：
     *       添加商品过期时间随机数。
     *       我们在同一时间批量设置这些商品的过期时间时，可以将过期时间设置不同。例如可以将过期时间随机加上几个小时，使这些商品缓存的过
     *       期时间不会在某一时间同时失效。
     * @return
     */
    private Integer genProductCacheTimeout(){
        return PRODUCT_CACHE_TIMEOUT + new Random().nextInt(5) * 60 * 60;
    }

    private Integer genEmptyCacheTimeout(){
        return 60 + new Random().nextInt(30);
    }
}
