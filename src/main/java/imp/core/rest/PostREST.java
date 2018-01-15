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
        System.out.println("imp.core.rest.PostREST.getAll()");
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
        System.out.println("imp.core.rest.PostREST.getById()");
        Post result = postRepository.getById(id);
        if (result != null) {
            return Response.ok(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Post not found for id: " + id)
                .build();
    }
    
    @POST
    @Path("newPostFor/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPost(@PathParam("id") Long id, Post json) {
        System.out.println("imp.core.rest.RecruiterREST.addPost()");
        Post result = postRepository.addPost(id, json);
        return Response.ok(result).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Post json) {
        System.out.println("imp.core.rest.PostREST.updatePost()");
        Post result = postRepository.getById(id);
        if (result != null) {
            result = postRepository.edit(id, json);
            return Response.ok(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Post not found for id: " + id)
                .build();
    }
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.PostREST.deletePost()");
        Post result = postRepository.getById(id);
        if (result != null) {
            postRepository.removeById(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Post not found for id: " + id)
                .build();
    }

}
