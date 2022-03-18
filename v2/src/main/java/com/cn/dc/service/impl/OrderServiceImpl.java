package com.cn.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cn.dc.biz.constant.CacheConstant;
import com.cn.dc.biz.entity.Order;
import com.cn.dc.biz.mapper.OrderMapper;
import com.cn.dc.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    private RedisTemplate redisTemplate;

    @Override
    @Cacheable(value = "order",key = "#id")
//    @Cacheable(cacheNames = "order",key = "#p0")
    public Order getOrderById(Long id) {
        String key= CacheConstant.ORDER + id;

        //先查询 Redis
        Object obj = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(obj)){
            log.info("get data from redis");
            return (Order) obj;
        }

        // Redis没有则查询 DB
        log.info("get data from database");
        Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, id));
        redisTemplate.opsForValue().set(key,myOrder,120, TimeUnit.SECONDS);

        return myOrder;
    }

    @Override
    @CachePut(cacheNames = "order",key = "#order.id")
    public Order updateOrder(Order order) {
        log.info("update order data");
        orderMapper.updateById(order);
        //修改 Redis
        redisTemplate.opsForValue().set(CacheConstant.ORDER + order.getId(),
                order, 120, TimeUnit.SECONDS);
        return order;
    }

    @Override
    @CacheEvict(cacheNames = "order",key = "#id")
    public void deleteOrder(Long id) {
        log.info("delete order");
        orderMapper.deleteById(id);
        redisTemplate.delete(CacheConstant.ORDER + id);
    }
}
