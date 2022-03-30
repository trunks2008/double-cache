package com.cn.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cn.dc.biz.constant.CacheConstant;
import com.cn.dc.biz.entity.Order;
import com.cn.dc.biz.mapper.OrderMapper;
import com.cn.dc.cache.DoubleCacheManager;
import com.cn.dc.service.OrderService;
import com.cn.dc.util.SpringContextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;
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

    @Override
    @Cacheable(value = "order",key = "#id")
    public Order getOrderById(Long id) {
        Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, id));
        return myOrder;
    }

    @Override
    @CachePut(cacheNames = "order",key = "#order.id")
    public Order updateOrder(Order order) {
        orderMapper.updateById(order);
        return order;
    }

    @Override
    @CacheEvict(cacheNames = "order",key = "#id")
    public void deleteOrder(Long id) {
        orderMapper.deleteById(id);
    }


    @Override
    public Order getOrderById2(Long id) {
        DoubleCacheManager cacheManager = SpringContextUtil.getBean(DoubleCacheManager.class);
        Cache cache = cacheManager.getCache("order");
        Order order =(Order) cache.get(id, (Callable<Object>) () -> {
            log.info("get data from database");
            Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                    .eq(Order::getId, id));
            return myOrder;
        });
        return order;
    }
}
