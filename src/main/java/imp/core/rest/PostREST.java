/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.PostRepository;
import imp.core.entity.Exemple;
import imp.core.entity.Post;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
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
        return Response.ok(postRepo.getById(id)).build();
    }
    
        /**
     * Only for test purposes
     * @return 
     */
    @GET
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add() {
        Post p = postRepo.add();
        return Response.ok(p).build();
    }
    
}
