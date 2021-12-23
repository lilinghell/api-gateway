package com.hell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "http.client.http-pool")
public class HttpClientProperties {
    //默认配置
    private int defaultMaxPerRoute = 200;
    private int maxTotal = 400;
    private int validateAfterInactivity = 2000;
    private int connectTimeout = 2000;
    private int connectionRequestTimeout = 5000;
    private int socketTimeout = 10000;
}
