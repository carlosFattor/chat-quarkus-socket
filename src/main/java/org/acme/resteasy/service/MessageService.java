package org.acme.resteasy.service;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;

import javax.enterprise.context.ApplicationScoped;

import org.acme.resteasy.dto.MessageTO;

@ApplicationScoped
public class MessageService {
    private static final BroadcastProcessor<MessageTO> BROADCASTER = BroadcastProcessor.create();

    public MessageTO save(final MessageTO message) {
        notifySave(message);
        return message;
    }

    public Multi<MessageTO> notifySave(final MessageTO message) {
        if (message != null) {
            BROADCASTER.onNext(message);
        }
        return BROADCASTER;
    }

}
