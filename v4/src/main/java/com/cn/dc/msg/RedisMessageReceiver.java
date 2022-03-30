package com.cn.dc.msg;

import com.cn.dc.cache.DoubleCache;
import com.cn.dc.cache.DoubleCacheManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-26 09:47
 **/
@Slf4j
@Component
@AllArgsConstructor
public class RedisMessageReceiver {
    private final RedisTemplate redisTemplate;
    private final DoubleCacheManager manager;

    //接收通知，进行处理
    public void receive(String message) throws UnknownHostException {
        CacheMassage msg = (CacheMassage) redisTemplate
                .getValueSerializer().deserialize(message.getBytes());
        log.info(msg.toString());

        //如果是本机发出的消息，那么不进行处理
        if (msg.getMsgSource().equals(MessageSourceUtil.getMsgSource())){
            log.info("收到本机发出的消息，不做处理");
            return;
        }

        DoubleCache cache = (DoubleCache) manager.getCache(msg.getCacheName());
        if (msg.getType()== CacheMsgType.UPDATE) {
            cache.updateL1Cache(msg.getKey(),msg.getValue());
            log.info("更新本地缓存");
        }

        if (msg.getType()== CacheMsgType.DELETE) {
            log.info("删除本地缓存");
            cache.evictL1Cache(msg.getKey());
        }
    }
}
