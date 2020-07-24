package com.redisdemo.pulgins.redis.pubSub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {
    @Autowired
    SubReceiverA aSubReceiver;
    @Autowired
    SubReceiverB bSubReceiver;

    @Bean("messageListenerAdapterA")
    MessageListenerAdapter messageListenerAdapterA() {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(aSubReceiver);
        return messageListenerAdapter;
    }
    @Bean("messageListenerAdapterB")
    MessageListenerAdapter messageListenerAdapterB() {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(bSubReceiver);
        return messageListenerAdapter;
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, @Qualifier("messageListenerAdapterA") MessageListenerAdapter listenerAdapterA, @Qualifier("messageListenerAdapterB") MessageListenerAdapter listenerAdapterB) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterA, new PatternTopic("boo*"));
        container.addMessageListener(listenerAdapterB, new ChannelTopic("book"));
        return container;

    }
}
