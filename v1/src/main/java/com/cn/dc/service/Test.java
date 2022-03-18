package com.cn.dc.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-18 09:59
 **/
public class Test {
    public static void main(String[] args) {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .initialCapacity(128)//初始大小
                .maximumSize(1024)//最大数量
                .expireAfterWrite(60, TimeUnit.SECONDS)//过期时间
                .build();

        cache.put("key1","value1");
//        cache.put("key2",null);

        System.out.println(cache.getIfPresent("key1"));
    }
}
