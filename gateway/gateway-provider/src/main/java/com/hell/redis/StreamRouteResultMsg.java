package com.hell.redis;

import com.hell.dao.SMessageDao;
import com.hell.db.table.provider.SMessage;
import com.hell.entity.Message;
import com.hell.entity.api.AckApiRoute;
import com.hell.service.MessageService;
import com.hell.core.common.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

/**
 * Stream consuming 路由信息
 */
public class StreamRouteResultMsg implements StreamListener<String, ObjectRecord<String, String>> {
    protected Log log = LogFactory.getLog(this.getClass());

    private MessageService messageService;
    private SMessageDao messageDao;

    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        try {
            AckApiRoute ackApiRoute = Utils.jsonToPojo(message.getValue(), AckApiRoute.class);
            SMessage mess = new SMessage();
            mess.setType("0");//路由通知信息
            mess.setTitle("route通知");
            mess.setStatus("1");//未阅读
            mess.setEntSeq(ackApiRoute.getEntSeq());
            mess.setInfo(message.getValue());

            messageDao.save(mess);
            Message m = new Message();
            m.setType("0");
            m.setVal(mess);
            messageService.notifyMsg(m);
        } catch (Exception e) {
            log.error("====route通知处理失败====");
            e.printStackTrace();
        }
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setMessageDao(SMessageDao messageDao) {
        this.messageDao = messageDao;
    }
}
