package com.hell.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ApplicationEventPublisher publisher;

    protected RouteLocator routeLocator;
    private RouteDefinitionWriter routeDefinitionWriter;


    /**
     * 添加及更新
     */
    public boolean refresh(RouteDefinition definition) {
        try {
            this.routeLocator.getRoutes().flatMap((route) -> {
                //先判断是否存在，存在就删除
                if (route.getId().equals(definition.getId())) {
                    log.info("refresh delete对应的路由ID :{}", definition.getId());
                    routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();
                }
                return Mono.empty();
            }).subscribe();
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            log.info("refresh add 成功,路由ID: {}", definition.getId());
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("refresh 失败,路由ID: {}", definition.getId());
            return false;
        }
        return true;
    }

    /**
     * 删除
     */
    public void delete(String routeId) {
        this.routeLocator.getRoutes().flatMap((route) -> {
            //判断是否存在，存在就删除
            if (route.getId().equals(routeId)) {
                log.info("delete对应的路由ID成功 :{}", routeId);
                routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
                this.publisher.publishEvent(new RefreshRoutesEvent(this));
            } else {
                log.info("没有对应的路由ID,delete成功 :{}", routeId);
            }
            return Mono.empty();
        }).subscribe();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Autowired
    public void setRouteDefinitionWriter(RouteDefinitionWriter routeDefinitionWriter) {
        this.routeDefinitionWriter = routeDefinitionWriter;
    }

    @Autowired
    public void setRouteLocator(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }
}
