/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.ConversationRepository;
import imp.core.bean.RecruiterRepository;
import imp.core.entity.conversation.Conversation;
import imp.core.entity.conversation.Message;
import imp.core.rest.filter.JWTTokenNeeded;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Leopold
 */
@Stateless
@Path("conversations")
public class ConversationREST {
    
    @EJB
    private ConversationRepository conversationRepository;
    
    @EJB
    private RecruiterRepository recruiterRepository;
    
    /**
     * return all conversations availables for an user
     * 
     * @param id
     * @return 
     */
    @GET
    @Path("forUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded(pathParam = "id")
    public Response getConversationsByUserId(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.ConversationREST.getConversationByUserId()");
        
        List<Conversation> conversations = conversationRepository.findByUserId(id);
        GenericEntity<List<Conversation>> gen = new GenericEntity<List<Conversation>>(conversations) {};
        return Response.ok(gen).build();
    }
    
    /**
     * create a new message from user id1 to user id2
     * @param id1
     * @param id2
     * @param message
     * @return 
     */
    @POST
    @Path("{id}/{id2}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded(pathParam = "id")
    public Response addMessage(@PathParam("id") Long id1, @PathParam("id2") Long id2, Message message) {
        System.out.println("imp.core.rest.ConversationREST.addMessage()");
        
        Conversation conversation = conversationRepository.addMessage(message, id1, id2);
        return Response.ok(conversation).build();
    }
    
    /**
     * create a new message from user idUser and the owner of the post idPost
     * @param idUser
     * @param idPost
     * @param message
     * @return 
     */
    @POST
    @Path("{id}/ownerOf/{idPost}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded(pathParam = "id")
    public Response sendMessage(@PathParam("id") Long idUser, @PathParam("idPost") Long idPost, Message message) {
        System.out.println("imp.core.rest.ConversationREST.sendMessage()");
        
        Conversation conversation = conversationRepository.addMessage(
                message,
                idUser,
                recruiterRepository.findOwnerIdOfThePost(idPost)
        );
        return Response.ok(conversation).build();
    }
    
}
