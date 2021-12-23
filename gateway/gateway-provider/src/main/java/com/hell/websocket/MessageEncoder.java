package com.hell.websocket;

import com.hell.core.common.utils.Utils;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Object> {
    @Override
    public String encode(Object message) throws EncodeException {
        try {
            return Utils.objectToJson(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
