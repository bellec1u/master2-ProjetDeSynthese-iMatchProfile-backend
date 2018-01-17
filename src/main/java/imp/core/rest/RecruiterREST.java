/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.PostRepository;
import imp.core.bean.RecruiterRepository;
import imp.core.entity.post.Post;
import imp.core.entity.user.Recruiter;
import imp.core.rest.exception.ServiceException;
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

    @EJB
    private PostRepository postRepository;
    
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
        if (result == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Recruiter not found for id: " + id);
        }
        return Response.ok(result).build();
    }
    
    /**
     * Return all post of a recruiter
     *
     * @param id -- id of recruiter
     * @return
     */
    @GET
    @Path("{id}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsById(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.RecruiterREST.getPostsById()");
        // checking if the recruiter exists
        Recruiter recruiter = recruiterRepository.getById(id);
        if (recruiter == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Recruiter not found for id: " + id);
        }
        
        List<Post> postsRecruiter = recruiter.getPost();
        GenericEntity<List<Post>> posts = new GenericEntity<List<Post>>(postsRecruiter) {
        };
        return Response
                .ok(posts)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Recruiter recruiter) {
        Recruiter result = recruiterRepository.create(recruiter);
        return Response.ok(result).build();
    }

    @POST
    @Path(value = "{id}/posts")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response addPost(@PathParam(value = "id") Long id, Post json) {
        System.out.println("imp.core.rest.RecruiterREST.addPost()");
        // checking if the recruiter exists
        Recruiter recruiter = recruiterRepository.getById(id);
        if (recruiter == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Recruiter not found for id: " + id);
        }

        Post result = postRepository.addPost(id, json);
        return Response.ok(result).build();
    }
}
