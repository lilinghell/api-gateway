package com.hell.service;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SAppEnv;
import com.hell.entity.route.ApiRouteDao;
import com.hell.entity.route.NotifyApiRoute;
import com.hell.entity.route.NotifyRouteInfo;
import com.hell.core.common.utils.Utils;
import com.hell.core.exception.ValidationException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotifyGatewayService {
    private StringRedisTemplate redisTemplate;
    private ApiRouteDao apiRouteDao;
    private SAppEnvDao appEnvDao;

    public void notifyGateway(Integer apiSeq, String url, Integer appEnvSeq) throws Exception {
        SAppEnv appEnv = appEnvDao.findById(appEnvSeq).get();
        if(StringUtils.isEmpty(appEnv.getAddress()) && appEnv.getServiceType().equals(Dictionary.SERVICE_TYPE_0)) {
            throw new ValidationException(CheckMsg.VALIDATION_ENV_ADDRESS_NOT_NULl);
        }
        Map<String, Object> map = apiRouteDao.qryApiRoute(apiSeq);
        NotifyApiRoute apiRoute = Utils.jsonToPojo(Utils.objectToJson(map), NotifyApiRoute.class);
        NotifyRouteInfo notifyRouteInfo = new NotifyRouteInfo();
        if (ObjectUtils.isEmpty(map)) {
            //删除对应的路由
            notifyRouteInfo.setType("delete");
            apiRoute.setApiSeq(apiSeq);
            apiRoute.setUrl(url);
            apiRoute.setEntSeq(appEnv.getEntSeq());
            apiRoute.setEnvKey(appEnv.getEnvKey());
            notifyRouteInfo.setApiRoute(apiRoute);
        } else {
            notifyRouteInfo.setType("refresh");
            notifyRouteInfo.setApiRoute(apiRoute);
        }
        //redisTemplate.convertAndSend("routeChannel", Utils.objectToJson(notifyRouteInfo));
        ObjectRecord record = StreamRecords.objectBacked(Utils.objectToJson(notifyRouteInfo)).withStreamKey("routeStreamChannel");
        redisTemplate.opsForStream().add(record);
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setApiRouteDao(ApiRouteDao apiRouteDao) {
        this.apiRouteDao = apiRouteDao;
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
