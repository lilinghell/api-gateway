package com.hell.redis.mq;

import com.hell.redis.entity.Msg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 接受到到信息
 */
public class RecMessage implements StreamListener<String, ObjectRecord<String, Msg>>, InitializingBean {
    protected Log log = LogFactory.getLog(this.getClass());
    private StringRedisTemplate redisTemplate;
    private ThreadPoolTaskExecutor executor;

    @Override
    public void onMessage(ObjectRecord<String, Msg> message) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Msg msg = message.getValue();
                    log.info(message.getId() + "接受到信息： " + msg);
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    redisTemplate.opsForStream().delete("logStreamChannel", message.getId());
                }
            }
        });
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setExecutor(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(2);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("log-thread");
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }
}