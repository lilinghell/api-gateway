package com.hell.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/api/websocket/message", encoders = {MessageEncoder.class})
@Component
public class MessageWebSocket {

    protected Log log = LogFactory.getLog(this.getClass());
    private Session session;

    private static CopyOnWriteArraySet<MessageWebSocket> messageWebSockets =
            new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        messageWebSockets.add(this);
        log.info(this.session.getId() + "====连接加入！====");
    }

    @OnClose
    public void onClose() {
        log.info(this.session.getId() + "====连接关闭====");
        messageWebSockets.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info(this.session.getId() + "来自客户端的消息" + message);
    }

    public void sendMessage(Object message) throws EncodeException, IOException {
        this.session.getBasicRemote().sendObject(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info(this.session.getId() + "错误" + error.getCause().getMessage());
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static void setMessageWebSockets(CopyOnWriteArraySet<MessageWebSocket> messageWebSockets) {
        MessageWebSocket.messageWebSockets = messageWebSockets;
    }

    public static CopyOnWriteArraySet<MessageWebSocket> getMessageWebSockets() {
        return messageWebSockets;
    }
}
