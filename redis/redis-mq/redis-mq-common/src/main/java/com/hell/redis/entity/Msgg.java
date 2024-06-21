package com.hell.redis.entity;

import lombok.Data;

import java.util.Map;

@Data
public class Msgg {
    private String id;
    private String name;
    private Map<String, Object> dataMap;
}
