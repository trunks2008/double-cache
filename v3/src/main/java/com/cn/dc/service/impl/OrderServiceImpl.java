package com.cn.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cn.dc.annotation.CacheType;
import com.cn.dc.annotation.DoubleCache;
import com.cn.dc.biz.entity.Order;
import com.cn.dc.biz.mapper.OrderMapper;
import com.cn.dc.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 13:40
 **/
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    @Override
    @DoubleCache(cacheName = "order", key = "#id",
            type = CacheType.FULL)
    public Order getOrderById(Long id) {
        Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, id));
        return myOrder;
    }

    @Override
    @DoubleCache(cacheName = "order",key = "#order.id",
            type = CacheType.PUT)
    public Order updateOrder(Order order) {
        orderMapper.updateById(order);
        return order;
    }

    @Override
    @DoubleCache(cacheName = "order",key = "#id",
            type = CacheType.DELETE)
    public void deleteOrder(Long id) {
        orderMapper.deleteById(id);
    }

    @Override
    @DoubleCache(cacheName = "order",key = "#id")
    public Order getOrderByIdAndStatus(Long id,Integer status) {
        Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, id)
                .eq(Order::getStatus,status));
        return myOrder;
    }
}
