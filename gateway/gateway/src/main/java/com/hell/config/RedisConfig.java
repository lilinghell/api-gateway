package com.hell.config;

import com.hell.redis.StreamRouteMsg;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

@Configuration
public class RedisConfig {

    /*@Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(subRouteMsg());
    }

    @Bean
    RedisMessageListenerContainer routeContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListener(), new ChannelTopic("routeChannel"));
        return container;
    }

    @Bean
    public SubRouteMsg subRouteMsg() {
        return new SubRouteMsg();
    }*/


    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> routeStreamContainer(
            RedisConnectionFactory connectionFactory,
            StreamRouteMsg streamRouteMsg) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .batchSize(1000)
                .targetType(String.class)
                .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
                .create(connectionFactory, options);

        container.receive(StreamOffset.create("routeStreamChannel", ReadOffset.latest()), streamRouteMsg);
        container.start();
        return container;
    }

    @Bean
    public StreamRouteMsg streamRouteMsg() {
        return new StreamRouteMsg();
    }
}
