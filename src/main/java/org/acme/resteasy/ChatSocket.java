package org.acme.resteasy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.acme.resteasy.dto.MessageTO;
import org.acme.resteasy.dto.MessageTODecoder;
import org.acme.resteasy.dto.MessageTOEncode;
import org.acme.resteasy.service.MessageService;
import org.jboss.logging.Logger;

@ServerEndpoint(value = "/chat/{username}", subprotocols = {"username"}, encoders = MessageTOEncode.class, decoders = MessageTODecoder.class)
@ApplicationScoped
public class ChatSocket {

    private static final Logger LOG = Logger.getLogger(ChatSocket.class);

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    Jsonb jsonb;
    
    @Inject
    private MessageService messageService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String userName) {
        sessions.put(userName, session);
        var message = new MessageTO();
        message.setMsg("Join");
        message.setUserName(userName);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String userName) {
        sessions.remove(userName);
        var message = new MessageTO();
        message.setUserName(userName);
        message.setMsg("User " + userName + " lesft the room!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String userName, Throwable throwable) {
        sessions.remove(userName);
        LOG.error("onError", throwable);
        var message = new MessageTO();
        message.setUserName(userName);
        message.setMsg("Erro with user " + userName);
        broadcast(message);
    }

    @OnMessage
    public void onMessage(MessageTO message, @PathParam("username") String username) {
        broadcast(message);
    }

    private void broadcast(MessageTO message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

    @PostConstruct
    public void subscribe() {
        messageService.notifySave(null)
            .subscribe()
            .with(message -> {
                for(Map.Entry<String, Session>session: sessions.entrySet()) {
                    if(session.getKey().equals(message.getUserName())) {
                        session.getValue().getAsyncRemote().sendObject(message);
                    }
                }
            });
    }

}