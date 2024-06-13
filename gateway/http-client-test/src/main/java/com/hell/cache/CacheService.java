package com.hell.cache;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CacheService implements InitializingBean {
    private static final String _KEY_NAME_ = "_KEY_NAME";
    public static LoadingCache<String, Object> graphs = Caffeine.newBuilder()
//            .maximumSize(10000)
            .expireAfterWrite(Duration.ofSeconds(5))
            .build(key -> createExpensiveGraph(key));

    private static Object createExpensiveGraph(String key) {
        //todo 若没有，首次获取，则从数据库获取值返回
        String value = key + "liling";
        return value;
    }

    public void initAllCache() {
        for (int i = 0; i < 10; i++) {
            graphs.put(String.valueOf(i), i + 10);
        }
    }

    public Object getKey(String key) {
        return graphs.get(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initAllCache();
    }
}
