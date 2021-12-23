package com.hell.config;

import com.hell.balancer.EnvRoundRobinLoadBalancer;
import com.hell.dao.ApiRouteDao;
import com.hell.entity.route.NotifyApiRoute;
import com.hell.filters.global.EnvRoundRobinLoadBalancerFilter;
import com.hell.filters.global.LogGlobalFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.UrlBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class GatewayConfig {
    @Value("${spring.profiles.active:pro}")
    private String active;
    @Value("${server.port:8080}")
    private String serverPort;
    @Value("${mock.server.url:}")
    private String mockServerUrl;
    private static final Set<String> statusCodes = new HashSet<>();

    static {
        statusCodes.add("500");
        statusCodes.add("NOT_FOUND");
    }

    private ApiRouteDao apiDao;

//    @Bean
//    public GlobalFilter customFilter() {
//        return new LogGlobalFilter();
//    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCircuitBreakerCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                //65秒后端未返回就熔断
                .timeLimiterConfig(TimeLimiterConfig.custom().
                        timeoutDuration(Duration.ofSeconds(65)).build()).build());
    }

   /* @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, JsonProcessingException {
        // 静态装载路由信息，装载后无法动态删除
        RouteLocatorBuilder.Builder routeLocatorBuilder = builder.routes();
        List<Map<String, Object>> list = apiDao.qryApiRoute(active);
        List<ApiRoute> apiRouteList = Utils.jsonToList(Utils.objectToJson(list), ApiRoute.class);
        for (ApiRoute apiRoute : apiRouteList) {
            initRoute(routeLocatorBuilder, apiRoute);
        }
        return routeLocatorBuilder.build();
    }*/

    private void initRoute(RouteLocatorBuilder.Builder routeLocatorBuilder, NotifyApiRoute apiRoute) {
        if ("0".equals(String.valueOf(apiRoute.getServiceType()))) {//HTTP服务
            Map<String, Object> metadata = new HashMap<>();
            if (ObjectUtils.isNotEmpty(apiRoute.getConnectTimeout())) {
                metadata.put("connect-timeout", apiRoute.getConnectTimeout());
            }
            if (ObjectUtils.isNotEmpty(apiRoute.getResponseTimeout())) {
                metadata.put("response-timeout", apiRoute.getResponseTimeout());
            }

            if ("0".equals(apiRoute.getMockSwitch())) {
                //mock
                String uri = "http://localhost:" + serverPort;
                if (!"".equals(mockServerUrl)) {
                    uri = mockServerUrl;
                }
                String finalUri = uri;

                String mockInfo = ObjectUtils.isEmpty(apiRoute.getApiMock()) ? ""
                        : apiRoute.getApiMock();
                mockInfo = new String(UrlBase64.encode(mockInfo.getBytes()));
                String finalMockInfo = mockInfo;
                routeLocatorBuilder.route("route_id_" + apiRoute.getApiSeq(),
                        r -> r.path(apiRoute.getUrl())
                                .filters(f -> {
                                    if (!"".equals(mockServerUrl)) {
                                        f.setPath("/api/mock")
                                                .addRequestParameter("mockInfo", apiRoute.getApiSeq().toString());
                                    }
                                    f.setPath("/api/mock")
                                            .addRequestParameter("mockInfo", finalMockInfo);
                                    return f;
                                })
                                .metadata(metadata)
                                .uri(finalUri));
            } else {
                routeLocatorBuilder.route("route_id_" + apiRoute.getApiSeq(),
                        r -> r.path(apiRoute.getUrl())
                                .filters(f -> {
                                    if ("0".equals(apiRoute.getFlowSwitch()) && StringUtils.isNotEmpty(apiRoute.getReplenishRate())
                                            && StringUtils.isNotEmpty(apiRoute.getBurstCapacity())) {
                                        //限流,状态: 429 Too Many Requests
                                        f.requestRateLimiter().rateLimiter(RedisRateLimiter.class,
                                                c -> c.setReplenishRate(Integer.valueOf(apiRoute.getReplenishRate()))
                                                        .setReplenishRate(Integer.valueOf(apiRoute.getBurstCapacity())))
                                                .configure(c -> c.setKeyResolver(methodKeyResolver()));
                                    }
                                    f.circuitBreaker(c -> c.setName("fallbackCircuitBreaker")
                                            .setFallbackUri("forward:/error/fallback")//超时、500、NOT_FOUND 熔断降级
                                            .setStatusCodes(statusCodes)
                                    );
                                    return f;
                                })
                                .metadata(metadata)//超时时间
                                .uri(apiRoute.getAddress())
                );
            }
        } else {
            //微服务 todo
        }
    }

    @Bean
    KeyResolver methodKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getMethod().name());
    }


    @Autowired
    public void setApiDao(ApiRouteDao apiDao) {
        this.apiDao = apiDao;
    }
}
