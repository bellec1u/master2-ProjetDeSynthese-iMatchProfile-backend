/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.NotificationRepository;
import imp.core.entity.user.Notification;
import imp.core.rest.exception.ServiceException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Mohamed
 */

@Stateless
@Path("notifications")
public class NotificationREST {
    
    @EJB
    private NotificationRepository notificationRepository;
    
    
    @GET
    @Path("user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotificationsById(@PathParam("id") Long id) {
        List<Notification> result = notificationRepository.getNotificationsByUser(id);
        GenericEntity<List<Notification>> notifications = new GenericEntity<List<Notification>>(result) {
        };
        return Response
                .ok(notifications)
                .build();
    }
    
    @GET
    @Path("user/{id}/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNoReadNotificationCount(@PathParam("id") Long id){
        long count = notificationRepository.getCountNoReadNotification(id);
        String c = "{\"count\": " + count + "}";
        return Response.ok(c).build();
    } 
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateNotification(@PathParam("id") Long id, Notification notification) {
        Notification result = notificationRepository.getById(id);
        // if the notification to update does not exist
        if (result == null) {   // return a 404
            throw new ServiceException(Response.Status.NOT_FOUND, "Notification not found for id: " + id);
        }
        
        result = notificationRepository.edit(notification);
        return Response.ok(result).build();
    }
    
    
    
}
