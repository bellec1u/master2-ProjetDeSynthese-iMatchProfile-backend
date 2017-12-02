/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.ExempleRepository;
import imp.core.entity.Exemple;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Exemple> list = exempleRepo.getAll();
        // because ok() method expects an Entity as parameter
        GenericEntity<List<Exemple>> gen = new GenericEntity<List<Exemple>>(list) {};
        return Response.ok(gen).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(exempleRepo.getById(id)).build();
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
        return Response.ok().build();
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
