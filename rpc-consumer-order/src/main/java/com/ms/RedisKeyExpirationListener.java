package com.ms;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * redis key过期监听
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 处理过期数据
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern){
        //获取到失效的key进行业务处理
        String expireKey = message.toString();
        System.out.println("key "+expireKey+"expire...");
    }
}
