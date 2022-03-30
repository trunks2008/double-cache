package com.cn.dc.controller;

import com.cn.dc.biz.entity.Order;
import com.cn.dc.cache.DoubleCache;
import com.cn.dc.cache.DoubleCacheManager;
import com.cn.dc.service.OrderService;
import com.cn.dc.util.SpringContextUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 13:32
 **/
@RestController
@RequestMapping("order")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @GetMapping("get/{id}")
    public Order get(@PathVariable("id") Long id){
        return orderService.getOrderById(id);
    }

    @PostMapping("update")
    public void updateOrder(@RequestBody Order order){
        orderService.updateOrder(order);
    }

    @DeleteMapping("del")
    public void del(@RequestParam("id") Long id){
        orderService.deleteOrder(id);
    }

    @GetMapping("get2/{id}")
    public Order get2(@PathVariable("id") Long id){
        return orderService.getOrderById2(id);
    }

    @GetMapping("clear")
    public void clearCache() throws Exception {
        DoubleCacheManager cacheManager = SpringContextUtil.getBean(DoubleCacheManager.class);
        DoubleCache doubleCache = (DoubleCache) cacheManager.getCache("order");

        Class<DoubleCache> doubleCacheClass = DoubleCache.class;
        Field caffeineCacheField = doubleCacheClass.getDeclaredField("caffeineCache");
        caffeineCacheField.setAccessible(true);
        Cache caffeineCache = (Cache) caffeineCacheField.get(doubleCache);
        System.out.println(caffeineCache.asMap());

        doubleCache.clear();
        System.out.println("clear...");

        System.out.println(caffeineCache.asMap());
    }


    @GetMapping("name")
    public void cacheName(){
        DoubleCacheManager cacheManager = SpringContextUtil.getBean(DoubleCacheManager.class);
        Collection<String> cacheNames = cacheManager.getCacheNames();
        System.out.println(cacheNames);
    }

}
