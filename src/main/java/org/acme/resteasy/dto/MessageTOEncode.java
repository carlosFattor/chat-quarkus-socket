package org.acme.resteasy.dto;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageTOEncode implements Encoder.Text<MessageTO> {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public void init(EndpointConfig config) {
        System.out.println(config);
    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(MessageTO object) {
        return gson.toJson(object);
    }
}
