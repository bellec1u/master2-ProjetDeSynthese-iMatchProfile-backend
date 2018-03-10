/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.ExempleRepository;
import imp.core.bean.authentication.AuthManager;
import imp.core.entity.Exemple;
import imp.core.rest.exception.ServiceException;
import imp.core.rest.filter.JWTTokenNeeded;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author auktis
 */
@Stateless
@Path("exemples")
public class ExempleREST {
    
    @EJB
    private ExempleRepository exempleRepo;
    
    @EJB
    private AuthManager authManager;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Exemple> list = exempleRepo.getAll();
        // because json mashalling is not defined for Collection classes
        GenericEntity<List<Exemple>> gen = new GenericEntity<List<Exemple>>(list) {};
        return Response.ok(gen).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Exemple ex = exempleRepo.getById(id);
        if (ex == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Exemple not found");
        }
        return Response.ok(ex).build();
    }
    
    @GET
    @Path("{id}/auth")
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response getByIdAuth(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        if (!authManager.hasTokenSameId(token, id)) {
            throw new ServiceException(Response.Status.UNAUTHORIZED, "Authentication is required");
        }
        
        Exemple ex = exempleRepo.getById(id);
        if (ex == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Exemple not found");
        }
        return Response.ok(ex).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(@PathParam("id") Long id, Exemple entity) {
        exempleRepo.edit(entity);
        return Response.ok().build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Exemple entity) {
        Exemple ex = exempleRepo.create(entity);
        return Response.ok(ex).status(Response.Status.CREATED).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Long id) {
        exempleRepo.removeById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    
    /**
     * Only for test purposes
     * @return 
     */
    @GET
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add() {
        Exemple ex = exempleRepo.add();
        return Response.ok(ex).build();
    }
    
    
}
