package com.hell.service;

import com.hell.entity.Message;
import com.hell.websocket.MessageWebSocket;
import org.springframework.stereotype.Service;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class MessageService implements Serializable {
    public void notifyMsg(Message message) {
        try {
            CopyOnWriteArraySet<MessageWebSocket> a = MessageWebSocket.getMessageWebSockets();
            a.forEach(m -> {
                try {
                    m.sendMessage(message);
                } catch (EncodeException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
