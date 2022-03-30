package com.cn.dc.config;

import com.cn.dc.msg.RedisMessageReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-30 11:17
 **/
@Configuration
public class MessageConfig {
    public static final String TOPIC="cache.msg";

    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter,
                                            RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(TOPIC));
        return container;
    }

    @Bean
    MessageListenerAdapter adapter(RedisMessageReceiver receiver){
        return new MessageListenerAdapter(receiver,"receive");
    }
}
