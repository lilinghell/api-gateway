package com.hell.redis;

import com.hell.common.Utils;
import com.hell.entity.route.AckApiRoute;
import com.hell.entity.route.NotifyRouteInfo;
import com.hell.service.RouteService;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import java.net.InetAddress;

/**
 * Stream consuming 路由信息
 */
public class StreamRouteMsg implements StreamListener<String, ObjectRecord<String, String>> {
    protected Log log = LogFactory.getLog(this.getClass());

    private RouteService routeService;
    private StringRedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        log.info("接收数据:" + message.getValue());
        boolean status = true;
        String errorMsg = "";
        NotifyRouteInfo routeInfo = null;
        try {
            routeInfo = Utils.jsonToPojo(message.getValue(), NotifyRouteInfo.class);
            routeService.customRouteLocator(routeInfo);
        } catch (Exception e) {
            log.error(e);
            errorMsg = e.getCause().getMessage();
            status = false;
            e.printStackTrace();
        } finally {
            //通知结果
            AckApiRoute ackApiRoute = new AckApiRoute();
            assert routeInfo != null;
            ackApiRoute.setApiSeq(routeInfo.getApiRoute().getApiSeq());
            ackApiRoute.setApiUrl(routeInfo.getApiRoute().getUrl());
            ackApiRoute.setType(routeInfo.getType());
            ackApiRoute.setStatus(status);
            ackApiRoute.setErrorMsg(errorMsg);
            ackApiRoute.setEnvKey(routeInfo.getApiRoute().getEnvKey());
            ackApiRoute.setHostIp(InetAddress.getLocalHost().getHostAddress());
            ackApiRoute.setEntSeq(routeInfo.getApiRoute().getEntSeq());
            String result = Utils.objectToJson(ackApiRoute);

            ObjectRecord record = StreamRecords.objectBacked(result)
                    .withStreamKey("routeResultStreamChannel");
            redisTemplate.opsForStream().add(record);
        }
    }

    @Autowired
    public void setRouteService(RouteService routeService) {
        this.routeService = routeService;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
