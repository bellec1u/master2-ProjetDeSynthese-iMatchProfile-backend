/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.RecruiterRepository;
import imp.core.entity.post.Post;
import imp.core.entity.user.Recruiter;
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
 * @author Mohamed
 */
@Stateless
@Path("recruiters")
public class RecruiterREST {

    @EJB
    private RecruiterRepository recruiterRepository;

    /**
     * Returns all the recruiters.
     *
     * @return A response containing all the recruiters.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        System.out.println("imp.core.rest.RecruiterREST.getAll()");
        List<Recruiter> list = recruiterRepository.getAll();
        GenericEntity<List<Recruiter>> recruiters = new GenericEntity<List<Recruiter>>(list) {
        };
        return Response
                .ok(recruiters)
                .build();
    }

    /**
     * Return a recruiter and all his posts
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.RecruiterREST.getById()");
        Recruiter result = recruiterRepository.getById(id);
        if (result != null) {
            return Response.ok(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Recruiter not found for id: " + id)
                .build();
    }
    
    @POST
    @Path("{id}/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPost(@PathParam("id") Long id, Post json) {
        System.out.println("imp.core.rest.RecruiterREST.addPost()");
        Post result = recruiterRepository.addPost(id, json);
        return Response.ok(result).build();
    }
    
    /**
     * Return all post of a recruiter
     *
     * @param id -- id of recruiter
     * @return
     */
    @GET
    @Path("{id}/post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsById(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.RecruiterREST.getPostsById()");
        // checking if the recruiter exists
        Recruiter recruiter = recruiterRepository.getById(id);
        if (recruiter == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Recruiter not found for id: " + id)
                .build();
        }
        
        List<Post> postsRecruiter = recruiter.getPost();
        GenericEntity<List<Post>> posts = new GenericEntity<List<Post>>(postsRecruiter) {
        };
        return Response
                .ok(posts)
                .build();
    }

}
