/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.PostRepository;
import imp.core.entity.Exemple;
import imp.core.entity.Post;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Leopold
 */

@Stateless
@Path("posts")
public class PostREST {
    
    @EJB
    private PostRepository postRepo;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Post entity) {
        Post post = postRepo.create(entity);
        return Response.ok(post).status(Response.Status.CREATED).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(@PathParam("id") Long id, Post entity) {
        postRepo.edit(entity);
        return Response.ok().build();
    }
    
    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Long id) {
        postRepo.removeById(id);
        return Response.ok().build();
    }
    
}
