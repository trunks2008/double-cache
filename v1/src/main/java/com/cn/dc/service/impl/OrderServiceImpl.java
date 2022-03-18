package com.cn.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cn.dc.biz.entity.Order;
import com.cn.dc.biz.mapper.OrderMapper;
import com.cn.dc.biz.constant.CacheConstant;
import com.cn.dc.service.OrderService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 13:40
 **/
@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final Cache cache;
    private final RedisTemplate redisTemplate;

    @Override
    public Order getOrderById(Long id) {
        String key = CacheConstant.ORDER + id;
        Order order = (Order) cache.get(key,
                k -> {
                    //先查询 Redis
                    Object obj = redisTemplate.opsForValue().get(k);
                    if (Objects.nonNull(obj)) {
                        log.info("get data from redis");
                        return obj;
                    }

                    // Redis没有则查询 DB
                    log.info("get data from database");
                    Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                            .eq(Order::getId, id));
                    redisTemplate.opsForValue().set(k, myOrder, 120, TimeUnit.SECONDS);
                    return myOrder;
                });
//        Order order = (Order) cache.get(key, k -> getOrder((String) k));
        return order;
    }


    @Override
    public void updateOrder(Order order) {
        log.info("update order data");
        String key = CacheConstant.ORDER + order.getId();
        orderMapper.updateById(order);
        //修改 Redis
        redisTemplate.opsForValue().set(key, order, 120, TimeUnit.SECONDS);
        // 修改本地缓存
        cache.put(key, order);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("delete order");
        orderMapper.deleteById(id);
        String key = CacheConstant.ORDER + id;
        redisTemplate.delete(key);
        cache.invalidate(key);
    }

//    public Order getOrder(String k){
//        //先查询 Redis
//        Object obj = redisTemplate.opsForValue().get(k);
//        if (Objects.nonNull(obj)) {
//            System.out.println("get data from redis");
//            return (Order) obj;
//        }
//
//        // Redis没有则查询 DB
//        System.out.println("get data from database");
//        Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
//                .eq(Order::getId, k.replace(CacheConstant.ORDER,"")));
//        redisTemplate.opsForValue().set(k, myOrder, 120, TimeUnit.SECONDS);
//        return myOrder;
//    }
}
