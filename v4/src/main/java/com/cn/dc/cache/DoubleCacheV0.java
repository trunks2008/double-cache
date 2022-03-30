package com.cn.dc.cache;

import com.cn.dc.config.DoubleCacheConfig;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 这个是没有加消息的最初版本
 *
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-21 10:07
 **/
@Slf4j
public class DoubleCacheV0 extends AbstractValueAdaptingCache {
    private String cacheName;
    private RedisTemplate<Object, Object> redisTemplate;
    private Cache<Object, Object> caffeineCache;
    private DoubleCacheConfig doubleCacheConfig;

    protected DoubleCacheV0(boolean allowNullValues) {
        super(allowNullValues);
    }

    public DoubleCacheV0(String cacheName, RedisTemplate<Object, Object> redisTemplate,
                         Cache<Object, Object> caffeineCache,
                         DoubleCacheConfig doubleCacheConfig){
        super(doubleCacheConfig.getAllowNull());
        this.cacheName =cacheName;
        this.redisTemplate=redisTemplate;
        this.caffeineCache=caffeineCache;
        this.doubleCacheConfig=doubleCacheConfig;
    }

    //使用注解时不走这个方法，实际走父类的get方法
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ReentrantLock lock=new ReentrantLock();
        try{
            lock.lock();//加锁

            Object obj = lookup(key);
            if (Objects.nonNull(obj)){
                return (T)obj;
            }
            //没有找到
            obj = valueLoader.call();
            //放入缓存
            put(key,obj);
            return (T)obj;
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    protected Object lookup(Object key) {
        // 先从caffeine中查找
        Object obj = caffeineCache.getIfPresent(key);
        if (Objects.nonNull(obj)){
            log.info("get data from caffeine");
            return obj; //不用fromStoreValue，否则返回的是null，会再查数据库
        }

        //再从redis中查找
        String redisKey=this.cacheName +":"+ key;
        obj = redisTemplate.opsForValue().get(redisKey);
        if (Objects.nonNull(obj)){
            log.info("get data from redis");
            caffeineCache.put(key,obj);
        }
        return obj;
    }

    @Override
    public void put(Object key, Object value) {
        if(!isAllowNullValues() && Objects.isNull(value)){
            log.error("the value NULL will not be cached");
            return;
        }

        //使用 toStoreValue(value) 包装，解决caffeine不能存null的问题
        //caffeineCache.put(key,value);
        caffeineCache.put(key,toStoreValue(value));

        // null对象只存在caffeine中一份就够了，不用存redis了
        if (Objects.isNull(value))
            return;

        String redisKey=this.cacheName +":"+ key;
        Optional<Long> expireOpt = Optional.ofNullable(doubleCacheConfig)
                .map(DoubleCacheConfig::getRedisExpire);
        if (expireOpt.isPresent()){
            redisTemplate.opsForValue().set(redisKey,toStoreValue(value),
                    expireOpt.get(), TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(redisKey,toStoreValue(value));
        }
    }

    @Override
    public void evict(Object key) {
        redisTemplate.delete(this.cacheName +":"+ key);
        caffeineCache.invalidate(key);
    }

    @Override
    public void clear() {
        //如果是正式环境，避免使用keys命令
        Set<Object> keys = redisTemplate.keys(this.cacheName.concat(":*"));
        for (Object key : keys) {
            redisTemplate.delete(String.valueOf(key));
        }
        caffeineCache.invalidateAll();
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

}
