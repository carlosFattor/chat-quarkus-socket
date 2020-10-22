package org.acme.resteasy;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.resteasy.dto.MessageTO;
import org.acme.resteasy.service.MessageService;


@Path(MessageResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {
    
    static final String PATH = "/messages";

    private final MessageService messageService;

    @Inject
    Jsonb jsonb;

    @Inject
    public MessageResource(final MessageService messageService) {
        this.messageService = messageService;
    }

    @POST
    public Response save(final MessageTO message) {
        return Response
                .status(Response.Status.CREATED)
                .entity(messageService.save(message))
                .build();
    }

}
