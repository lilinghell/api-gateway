package com.hell.redis.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hell.redis.entity.Msg;
import com.hell.redis.entity.Msgg;
import com.hell.redis.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class ProductController {
    private StringRedisTemplate redisTemplate;

    @PostMapping(path = "/api/t1/send")
    public void sendMsg(@RequestBody Msg msg) throws JsonProcessingException {
        String key = "logNum";
        Map map = new HashMap();
        map.put("001", "001");
        Msgg m = new Msgg();
        m.setId("nihao");
        map.put("msg", m);

        for (int i = 0; i < 2; i++) {
            msg.setId(String.valueOf(i));
            msg.setDataMap(JsonUtils.objectToJson(map));
            ObjectRecord<String, Msg> record = StreamRecords.objectBacked(msg)
                    .withStreamKey("logStreamChannel");
            RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
            Long increment = entityIdCounter.getAndIncrement();

            redisTemplate.opsForStream().add(record);
        }
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
