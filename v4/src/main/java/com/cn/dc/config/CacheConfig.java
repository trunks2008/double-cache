package com.cn.dc.config;

import com.cn.dc.cache.DoubleCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-25 15:01
 **/
@Configuration
public class CacheConfig {
    @Autowired
    DoubleCacheConfig doubleCacheConfig;

    @Bean
    public DoubleCacheManager cacheManager(RedisTemplate<Object,Object> redisTemplate,
                                           DoubleCacheConfig doubleCacheConfig){
        return new DoubleCacheManager(redisTemplate,doubleCacheConfig);
    }
}
