package com.cn.dc.service;


import com.cn.dc.biz.entity.Order;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 13:31
 **/
public interface OrderService {

    Order getOrderById(Long id);

    Order updateOrder(Order order);

    void deleteOrder(Long id);

    Order getOrderById2(Long id);

}
