package com.hell.redis.mq;

import com.hell.redis.entity.Msg;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

@Configuration
public class RedisConfig {
    protected Log log = LogFactory.getLog(this.getClass());
    private static final String STREAM_KEY = "logStreamChannel";
    private static final String GROUP_NAME = "logStreamGroup";
    private static final String CONSUMER_NAME = "log-consumer";
    private StringRedisTemplate redisTemplate;

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, Msg>> logStreamContainer
            (RedisConnectionFactory connectionFactory, RecMessage recMessage) {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, Msg>> options
                = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().errorHandler(e -> {
                    log.error("Redis消费异常: " + e.getMessage());
                })
                .batchSize(1000)
                .targetType(Msg.class)
                .build();

        //创建组
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, GROUP_NAME);
        } catch (Exception e) {
            log.warn(e.getCause().getMessage());
        }

        StreamOffset<String> streamOffset = StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed());
        StreamMessageListenerContainer.StreamReadRequest<String> readRequest = StreamMessageListenerContainer.StreamReadRequest
                .builder(streamOffset)
                .cancelOnError(e -> {
                    log.error("连接异常" + e.getMessage());
                    // 查询超时，有可能时断网了，不能取消
                    boolean b = !(e instanceof QueryTimeoutException
                            || e instanceof RedisConnectionException
                            || e instanceof RedisConnectionFailureException
                            || e instanceof RedisCommandTimeoutException
                    );
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    return b;
                })
                .consumer(Consumer.from(GROUP_NAME, CONSUMER_NAME))
                .autoAcknowledge(true)
                .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, Msg>> container = StreamMessageListenerContainer
                .create(connectionFactory, options);

        container.register(readRequest, recMessage);
        //自动ack
        /*container.receiveAutoAck(
                Consumer.from(GROUP_NAME, CONSUMER_NAME),
                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed()),
                recMessage);*/
        container.start();
        return container;
    }

    @Bean
    public RecMessage recMessage() {
        return new RecMessage();
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
