package com.cn.dc.controller;

import com.cn.dc.biz.entity.Order;
import com.cn.dc.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
