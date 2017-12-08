/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.CandidateRepository;
import imp.core.entity.Candidate;
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
 * @author alexis
 */
@Stateless
@Path("candidates")
public class CandidateREST {

    @EJB
    private CandidateRepository candidateRepository;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Candidate> list = candidateRepository.getAll();
        // because ok() method expects an Entity as parameter
        GenericEntity<List<Candidate>> candidates = new GenericEntity<List<Candidate>>(list) {};
        return Response.ok(candidates).build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Candidate result = candidateRepository.getById(id);
        System.out.println(result);
        if (result != null) {
            return Response.ok(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Candidate not found for id: " + id)
                .build();
    }

}
