package com.hell.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
    private HttpClientProperties httpClientProperties;

    /**
     * 显示修改httpClient连接池参数
     */
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setDefaultMaxPerRoute(httpClientProperties.getDefaultMaxPerRoute());
        httpClientConnectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
        //官方推荐:检查永久链接的可用性，不推荐每次请求的时候才去检查
        httpClientConnectionManager.setValidateAfterInactivity(httpClientProperties.getValidateAfterInactivity());
        return httpClientConnectionManager;
    }

    /**
     * 设置请求配置
     */
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())  //从链接池获取连接的超时时间
                .setConnectTimeout(httpClientProperties.getConnectTimeout())    //与服务器连接超时时间，创建socket连接的超时时间
                .setSocketTimeout(httpClientProperties.getSocketTimeout())   //socket读取数据的超时时间，从服务器获取数据的超时时间
                .build();
    }

    /**
     * 实例化连接池，设置连接池管理器
     */
    @Bean
    public HttpClientBuilder httpClientBuilder(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //设置连接池
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        //设置超时时间
        httpClientBuilder.setDefaultRequestConfig(requestConfig());
        //定义连接管理器将由多个客户端实例共享。如果连接管理器是共享的，则其生命周期应由调用者管理，如果客户端关闭则不会关闭。
        httpClientBuilder.setConnectionManagerShared(true);
        //关闭重试策略
        httpClientBuilder.disableAutomaticRetries();
        return httpClientBuilder;
    }

    @Bean
    public CloseableHttpClient httpClient(HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }


    @Autowired
    public void setHttpClientProperties(HttpClientProperties httpClientProperties) {
        this.httpClientProperties = httpClientProperties;
    }
}
