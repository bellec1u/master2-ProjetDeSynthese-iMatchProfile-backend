/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.RecruiterRepository;
import imp.core.entity.Post;
import imp.core.entity.Recruiter;
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
 * @author Mohamed
 */
@Stateless
@Path("recruiters")
public class RecruiterREST {
    
    @EJB
    private RecruiterRepository recruiterRepo;
    
    @GET
    @Path("{id}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsById(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.RecruiterREST.getPostsById()");
        Recruiter result = recruiterRepo.getById(id);
        GenericEntity<List<Post>> gen = new GenericEntity<List<Post>>(result.getPost()) {};        
        return Response.ok(gen).build();

    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.RecruiterREST.getById()");
        Recruiter result = recruiterRepo.getById(id);      
        return Response.ok(result).build();

    }
    
}
