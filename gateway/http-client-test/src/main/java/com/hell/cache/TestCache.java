package com.hell.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hell.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCache {

    private CacheService cacheService;

    @RequestMapping(path = "/cache/{key}")
    Object getKey(@PathVariable String key) throws JsonProcessingException {

        return cacheService.getKey(key);
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
