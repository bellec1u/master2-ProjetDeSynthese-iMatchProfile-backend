/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.PostRepository;
import imp.core.entity.post.Post;
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
    private PostRepository postRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Post> list = postRepository.getAll();
        // because ok() method expects an Entity as parameter
        GenericEntity<List<Post>> gen = new GenericEntity<List<Post>>(list) {
        };
        return Response.ok(gen).build();
    }

    /**
     * Get post with id
     *
     * @param id of post
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Post result = postRepository.getById(id);
        if (result != null) {
            return Response.ok(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Post not found for id: " + id)
                .build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("id") Long id, Post json) {
        Post result = postRepository.edit(id, json);
        return Response.ok(result).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response deletePost(@PathParam("id") Long id) {
        postRepository.removeById(id);
        return Response.ok().build();
    }

}
