/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.RecruiterRepository;
import imp.core.entity.post.Post;
import imp.core.entity.user.Recruiter;
import java.util.Calendar;
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
     * @return A response containing all the candidates.
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
        return Response.ok(result).build();
    }
    
    @POST
    @Path("{id}/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPost(@PathParam("id") Long id, Post json) {
        json.setPublicationDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        System.out.println("imp.core.rest.RecruiterREST.addPost()");
        Post result = recruiterRepository.addPost(id, json);
        return Response.ok(result).build();
    }

}
