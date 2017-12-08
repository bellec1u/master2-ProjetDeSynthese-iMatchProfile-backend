/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.PostRepository;
import imp.core.entity.Post;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ejb.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 *
 * @author Leopold
 */

@Stateless
@Path("posts")
public class PostREST {
    
    @EJB
    private PostRepository postRepo;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Post> list = postRepo.getAll();
        // because ok() method expects an Entity as parameter
        GenericEntity<List<Post>> gen = new GenericEntity<List<Post>>(list) {};
        return Response.ok(gen).build();
    }
    
    /**
     * Get post with id
     * @param id of post
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Post result = postRepo.getById(id);
        if (result != null) {
            return Response.ok(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Post not found for id: " + id)
                .build();
    }

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
    /**
     * Only for test purposes ...
     * Return list post 
     * @param id _ recruiter id
     * @return 
     */
    @GET
    @Path("{id}/recruiter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsByRec(@PathParam("id") Long id) {
        List<Post> result = postRepo.getPostsByRecruiter(id);
        GenericEntity<List<Post>> gen = new GenericEntity<List<Post>>(result) {};
        return Response.ok(gen).build();
        
    }
    
}
