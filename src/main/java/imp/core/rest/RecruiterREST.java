/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.RecruiterRepository;
import imp.core.entity.post.Post;
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Recruiter;
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
    private RecruiterRepository recruiterRepository;
    
    /**
     * Returns all the recruiters.
     *
     * @return A response containing all the candidates.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Recruiter> list = recruiterRepository.getAll();
        GenericEntity<List<Recruiter>> recruiters = new GenericEntity<List<Recruiter>>(list) {
        };
        return Response
                .ok(recruiters)
                .build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Recruiter result = recruiterRepository.getById(id);
        return Response.ok(result).build();
    }
    
}
