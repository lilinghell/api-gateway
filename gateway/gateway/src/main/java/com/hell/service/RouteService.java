package com.hell.service;

import com.hell.common.Utils;
import com.hell.dao.ApiRouteDao;
import com.hell.entity.route.NotifyApiRoute;
import com.hell.entity.route.NotifyRouteInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.UrlBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteService implements Serializable, InitializingBean {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${server.port:8080}")
    private String serverPort;
    @Value("${mock.server.url:}")
    private String mockServerUrl;
    @Value("${server.servlet.context-path:}")
    private String contextPath;
    @Value("${spring.application.name}")
    private String gatewayName;

    private ApiRouteDao apiDao;
    private DynamicRouteServiceImpl dynamicRouteService;

    public void customRouteLocator(NotifyRouteInfo routeInfo) throws Exception {
        if ("delete".equals(routeInfo.getType())) {
            //删除
            deleteRoute(routeInfo);
        }
        if ("refresh".equals(routeInfo.getType())) {
            //添加/修改
            refreshRoute(routeInfo);
        }
    }

    private void deleteRoute(NotifyRouteInfo routeInfo) {
        NotifyApiRoute apiRoute = new NotifyApiRoute();
        BeanUtils.copyProperties(routeInfo.getApiRoute(), apiRoute);

        dynamicRouteService.delete("route_id_" + apiRoute.getApiSeq());
    }

    private void refreshRoute(NotifyRouteInfo routeInfo) throws Exception {
        NotifyApiRoute apiRoute = new NotifyApiRoute();
        try {
            BeanUtils.copyProperties(routeInfo.getApiRoute(), apiRoute);
            initRoute(apiRoute);
        } catch (Exception e) {
            log.error(apiRoute.getUrl() + " refreshRoute error:" + Utils.objectToJson(apiRoute));
            throw e;
        }
    }

    private void initRoute(NotifyApiRoute apiRoute) throws Exception {
        if (apiRoute.getServiceType().equals("1") && StringUtils.isEmpty(apiRoute.getServiceId())) {
            log.warn(apiRoute.getUrl() + " service id is null");
            return;
        }
        Map<String, Object> metadata = new HashMap();
        if (ObjectUtils.isNotEmpty(apiRoute.getConnectTimeout())) {
            metadata.put("connect-timeout", apiRoute.getConnectTimeout());
        }
        if (ObjectUtils.isNotEmpty(apiRoute.getResponseTimeout())) {
            metadata.put("response-timeout", apiRoute.getResponseTimeout());
        }
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId("route_id_" + apiRoute.getApiSeq());
        routeDefinition.setMetadata(metadata);
        routeDefinition.setOrder(-1);

        //PredicateDefinition
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        PredicateDefinition pathPredicate = new PredicateDefinition();
        pathPredicate.setName("Path");
        Map<String, String> pathArgsMap = new HashMap<>();
        String rootPath = StringUtils.isEmpty(contextPath) ? "/" + gatewayName : contextPath;
        if (apiRoute.getServiceType().equals("1")) {
            //微服务
            rootPath = rootPath + "/" + apiRoute.getServiceId();
        }
        pathArgsMap.put("_pathGenkey_" + apiRoute.getApiSeq(), rootPath + apiRoute.getUrl());
        pathPredicate.setArgs(pathArgsMap);
        predicateDefinitionList.add(pathPredicate);

        PredicateDefinition headPredicate = new PredicateDefinition();
        headPredicate.setName("Header");
        Map<String, String> headerArgsMap = new HashMap<>();
        headerArgsMap.put("header", "X-Request-EnvKey");
        headerArgsMap.put("regexp", apiRoute.getEnvKey());
        headPredicate.setArgs(headerArgsMap);

        predicateDefinitionList.add(headPredicate);
        routeDefinition.setPredicates(predicateDefinitionList);

        if ("1".equals(apiRoute.getStatus())) {
            //停用
            String uri = "http://localhost:" + serverPort;
            routeDefinition.setUri(URI.create(uri));
            List<FilterDefinition> filters = new ArrayList<>();
            FilterDefinition setPathFilter = new FilterDefinition();
            setPathFilter.setName("SetPath");
            Map<String, String> setPathArgsMap = new HashMap<>();
            setPathArgsMap.put("template", "/api/stop");
            setPathFilter.setArgs(setPathArgsMap);
            filters.add(setPathFilter);

            routeDefinition.setFilters(filters);
            dynamicRouteService.refresh(routeDefinition);
            return;
        }
        if ("0".equals(apiRoute.getMockSwitch())) {
            //mock
            String uri = "http://localhost:" + serverPort;
            if (!"".equals(mockServerUrl)) {
                uri = mockServerUrl;
            }
            String mockInfo = ObjectUtils.isEmpty(apiRoute.getApiMock()) ? " "
                    : apiRoute.getApiMock();
            mockInfo = new String(UrlBase64.encode(mockInfo.getBytes()));

            //uri
            routeDefinition.setUri(URI.create(uri));
            //filters
            //SetPath filter
            List<FilterDefinition> filters = new ArrayList<>();
            FilterDefinition setPathFilter = new FilterDefinition();
            setPathFilter.setName("SetPath");
            Map<String, String> setPathArgsMap = new HashMap<>();
            setPathArgsMap.put("template", "/api/mock");
            setPathFilter.setArgs(setPathArgsMap);
            filters.add(setPathFilter);
            //AddRequestParameter filter
            FilterDefinition addRequestParameterFilter = new FilterDefinition();
            addRequestParameterFilter.setName("AddRequestParameter");
            Map<String, String> addRequestParameterArgsMap = new HashMap<>();
            addRequestParameterArgsMap.put("name", "mockInfo");
            if (!"".equals(mockServerUrl)) {
                addRequestParameterArgsMap.put("value", apiRoute.getApiSeq().toString());
            } else {
                addRequestParameterArgsMap.put("value", mockInfo);
            }
            addRequestParameterFilter.setArgs(addRequestParameterArgsMap);
            filters.add(addRequestParameterFilter);


            routeDefinition.setFilters(filters);
            dynamicRouteService.refresh(routeDefinition);
        } else {
            // 非mock
            //uri
            routeDefinition.setUri(getUri(apiRoute));
            //filters

            List<FilterDefinition> filters = new ArrayList<>();
            if ("0".equals(apiRoute.getFlowSwitch()) && StringUtils.isNotEmpty(apiRoute.getReplenishRate())
                    && StringUtils.isNotEmpty(apiRoute.getBurstCapacity())) {
                //限流,状态: 429 Too Many Requests
                //RequestRateLimiter filter
                FilterDefinition requestRateLimiterFilter = new FilterDefinition();
                requestRateLimiterFilter.setName("RequestRateLimiter");
                Map<String, String> requestRateLimiterArgsMap = new HashMap<>();
                requestRateLimiterArgsMap.put("redis-rate-limiter.replenishRate",
                        String.valueOf(apiRoute.getReplenishRate()));
                requestRateLimiterArgsMap.put("redis-rate-limiter.burstCapacity",
                        String.valueOf(apiRoute.getBurstCapacity()));
                requestRateLimiterArgsMap.put("redis-rate-limiter.requestedTokens", "1");
                requestRateLimiterFilter.setArgs(requestRateLimiterArgsMap);
                filters.add(requestRateLimiterFilter);
            }

            FilterDefinition rewritePathFilter = new FilterDefinition();
            rewritePathFilter.setName("RewritePath");
            Map<String, String> rewritePathArgsMap = new HashMap<>();
            rewritePathArgsMap.put("regexp", rootPath + "/?(?<remaining>.*)");
            rewritePathArgsMap.put("replacement", "/${remaining}");
            rewritePathFilter.setArgs(rewritePathArgsMap);
            filters.add(rewritePathFilter);

            //CircuitBreaker熔断降级:超时、500、NOT_FOUND
            /*FilterDefinition circuitBreakerFilter = new FilterDefinition();
            circuitBreakerFilter.setName("CircuitBreaker");
            Map<String, String> circuitBreakerArgsMap = new HashMap<>();
            circuitBreakerArgsMap.put("name", "myCircuitBreaker");
            circuitBreakerArgsMap.put("fallbackUri", "forward:/error/fallback");
            circuitBreakerArgsMap.put("statusCodes", "INTERNAL_SERVER_ERROR");
            circuitBreakerFilter.setArgs(circuitBreakerArgsMap);
            filters.add(circuitBreakerFilter);*/

            routeDefinition.setFilters(filters);
            dynamicRouteService.refresh(routeDefinition);
        }
    }

    private URI getUri(NotifyApiRoute apiRoute) {
        if ("0".equals(String.valueOf(apiRoute.getServiceType()))) {//HTTP服务
            return URI.create(apiRoute.getAddress());
        } else {
            //微服务
            return URI.create("envLb://" + apiRoute.getServiceId());
        }
    }

    @Autowired
    public void setApiDao(ApiRouteDao apiDao) {
        this.apiDao = apiDao;
    }

    @Autowired
    public void setDynamicRouteService(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Map<String, Object>> list = apiDao.qryApiRoute();
        List<NotifyApiRoute> apiRouteList = Utils.jsonToList(Utils.objectToJson(list), NotifyApiRoute.class);
        for (NotifyApiRoute apiRoute : apiRouteList) {
            try {
                initRoute(apiRoute);
            } catch (Exception e) {
                log.error(apiRoute.getUrl() + " refreshRoute error:" + Utils.objectToJson(apiRoute));
            }

        }
    }
}
