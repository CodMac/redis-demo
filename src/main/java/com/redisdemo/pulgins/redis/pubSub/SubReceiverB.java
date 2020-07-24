package com.redisdemo.pulgins.redis.pubSub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class SubReceiverB implements MessageListener {

    @Autowired
    @Qualifier("zqitRdsTemp")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {

        byte[] channel = message.getChannel();
        byte[] body = message.getBody();
        try {
            String content = new String(body,"UTF-8");
            String address = new String(channel,"UTF-8");
            System.out.println("SubReceiverB-监听(" + content + ") from (" + address + ")");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
