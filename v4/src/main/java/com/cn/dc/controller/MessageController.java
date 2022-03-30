package com.cn.dc.controller;

import com.cn.dc.biz.entity.Order;
import com.cn.dc.config.MessageConfig;
import com.cn.dc.msg.CacheMassage;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-30 11:23
 **/
@RestController()
@RequestMapping("msg")
@AllArgsConstructor
public class MessageController {
    private final RedisTemplate redisTemplate;

    @PostMapping("send")
    public String send(@RequestBody CacheMassage msg){
        redisTemplate.convertAndSend(MessageConfig.TOPIC,msg);
        return "success";
    }

}
