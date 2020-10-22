package org.acme.resteasy.dto;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageTODecoder implements Decoder.Text<MessageTO> {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public MessageTO decode(String s) throws DecodeException {
        var msg = gson.fromJson(s, MessageTO.class);
        return msg;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }    
}
