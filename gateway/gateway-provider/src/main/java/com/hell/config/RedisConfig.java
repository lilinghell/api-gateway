package com.hell.config;

import com.hell.redis.StreamRouteResultMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

@Configuration
public class RedisConfig {
    protected Log log = LogFactory.getLog(this.getClass());
    private StringRedisTemplate redisTemplate;

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> routeStreamContainer(
            RedisConnectionFactory connectionFactory,
            StreamRouteResultMsg streamRouteResultMsg) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .batchSize(100)
                .targetType(String.class)
                .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
                .create(connectionFactory, options);

        String streamKey = "routeResultStreamChannel";
        String groupName = "routeResultGroup";
        String consumerName = "my-consumer";

        try {
            redisTemplate.opsForStream().createGroup(streamKey, groupName);
        } catch (Exception e) {
            log.info(e.getCause().getMessage());
        }
        container.receiveAutoAck(
                Consumer.from(groupName, consumerName),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                streamRouteResultMsg
        );
        container.start();
        return container;
    }

    @Bean
    public StreamRouteResultMsg streamRouteResultMsg() {
        return new StreamRouteResultMsg();
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
