package com.hell.cache;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.graph.Graph;

import java.time.Duration;

public class SessionCacheService {
    public static void main(String[] args) {
        LoadingCache<String, Object> graphs = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterAccess(Duration.ofMinutes(5))
                .build(key -> createExpensiveGraph(key));

        Object f = graphs.get("nihao");
        System.out.println(f + "");
        graphs.put("nihao", "ok");
        Object f2 = graphs.get("nihao");
        System.out.println(f2 + "");
    }

    private static Graph createExpensiveGraph(String key) {
        System.out.println("liling");
        return null;
    }
}
