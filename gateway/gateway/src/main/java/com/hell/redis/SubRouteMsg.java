package com.hell.redis;

import com.hell.common.Utils;
import com.hell.entity.route.NotifyRouteInfo;
import com.hell.service.RouteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * 订阅获取路由信息
 */
public class SubRouteMsg implements MessageListener {
    protected Log log = LogFactory.getLog(this.getClass());

    private RouteService routeService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.info("接收数据:" + message.toString());
        try {
            NotifyRouteInfo routeInfo = Utils.jsonToPojo(message.toString(), NotifyRouteInfo.class);
            routeService.customRouteLocator(routeInfo);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    @Autowired
    public void setRouteService(RouteService routeService) {
        this.routeService = routeService;
    }
}
