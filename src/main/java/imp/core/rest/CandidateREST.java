/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.CandidateRepository;
import imp.core.entity.user.Candidate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST service for the candidate entity.
 *
 * @author alexis
 */
@Stateless
@Path("candidates")
public class CandidateREST {

    @EJB
    private CandidateRepository candidateRepository;

    /**
     * Returns all the candidates.
     *
     * @return A response containing all the candidates.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Candidate> list = candidateRepository.getAll();
        // because ok() method expects an Entity as parameter
        GenericEntity<List<Candidate>> candidates = new GenericEntity<List<Candidate>>(list) {
        };
        return Response
                .ok(candidates)
                .build();
    }

    /**
     * Returns the candidate corresponding to the id parameter.
     *
     * @param id The id of the candidate to get.
     * @return A response containing the candidate or a 404 response if the
     * candidate with this id is not found.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Candidate result = candidateRepository.getById(id);
        if (result != null) {
            return Response.ok(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Candidate not found for id: " + id)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Candidate candidate) {
        Candidate result = candidateRepository.create(candidate);
        return Response.ok(result).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Candidate candidate) {
        Candidate result = candidateRepository.getById(id);
        // if the candidate to update does not exist
        if (result == null) {   // return a 404
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Candidate not found for id: " + id)
                .build();
        }
        
        result = candidateRepository.edit(candidate);
        return Response.ok(result).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        Candidate result = candidateRepository.getById(id);
        // if the candidate to delete does not exist
        if (result == null) {   // return a 404
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Candidate not found for id: " + id)
                .build();
        }
        
        candidateRepository.removeById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
