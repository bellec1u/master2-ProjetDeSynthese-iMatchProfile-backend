/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.AssociatedCandidateRepository;
import imp.core.bean.CandidateRepository;
import imp.core.bean.MatchingRepository;
import imp.core.bean.UserRepository;
import imp.core.entity.post.AssociatedCandidate;
import imp.core.entity.user.Candidate;
import imp.core.password.Passwords;
import imp.core.rest.exception.ServiceException;
import imp.core.rest.filter.JWTTokenNeeded;
import imp.core.rest.validator.CandidatePassword;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.Valid;
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
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

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

    @EJB
    private UserRepository userRepository;
    
    @EJB
    private AssociatedCandidateRepository associatedCandidateRepository;
    
    @EJB
    private MatchingRepository matchingRepository;
    
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
        if (result == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Candidate not found for id: " + id);
        }
        return Response.ok(result).build();
    }
    
    @GET
    @Path("{id}/offers")
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded(pathParam = "id")
    public Response getOffer(@PathParam("id") Long id) {
        List<AssociatedCandidate> list = associatedCandidateRepository.getOffer(id);
        GenericEntity<List<AssociatedCandidate>> associatedCandidates = new GenericEntity<List<AssociatedCandidate>>(list) {};

        return Response.ok(associatedCandidates).build();
    }

    @GET
    @Path("{id}/matchings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMatchings(@PathParam("id") Long id) throws ParseException {
        System.out.println("imp.core.rest.PostREST.getBySkills()");
        JSONArray json = matchingRepository.getByCandidateId(id);
        
        return Response.ok(json.toJSONString()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid @CandidatePassword(minSize = 6, message = "{user.password.notNull}") Candidate candidate) {        
        // checking if email is already used
        if (!userRepository.findByEmail(candidate.getUser().getEmail()).isEmpty()) {
            throw new ServiceException(Response.Status.CONFLICT, "Email already used: " + candidate.getUser().getEmail());
        }
        
        Candidate result = candidateRepository.create(candidate);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded(pathParam = "id")
    public Response update(@PathParam("id") Long id,
            @Valid @CandidatePassword(required = false, minSize = 6, message = "{user.password.min}") Candidate candidate) {
        Candidate result = candidateRepository.getById(id);
        // if the candidate to update does not exist
        if (result == null) {   // return a 404
            throw new ServiceException(Response.Status.NOT_FOUND, "Candidate not found for id: " + id);
        }
          
        // set the same password salt
        candidate.getUser().setPasswordSalt(result.getUser().getPasswordSalt());
        // if password is not set
        if (candidate.getUser().getPassword() == null) {
            // take the old one
            candidate.getUser().setPassword(result.getUser().getPassword());
        } else {    // hash the new password
            candidate.getUser().setPassword(Passwords.hash(candidate.getUser().getPassword(), result.getUser().getPasswordSalt()));
        }
        
        result = candidateRepository.edit(candidate);
        return Response.ok(result).build();
    }
    
    @PUT
    @Path("{id}/offers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded(pathParam = "id")
    public Response acceptOffer(@PathParam("id") Long id) {
        AssociatedCandidate result = associatedCandidateRepository.getById(id);
        if (result == null) {   // return a 404
            throw new ServiceException(Response.Status.NOT_FOUND, "Offer not found for id: " + id);
        }
        result.setState(AssociatedCandidate.AssociatedState.ACCEPTER);
        associatedCandidateRepository.edit(result);
        return Response.ok(result).build();
    }
    
    @DELETE
    @Path("{id}")
    @JWTTokenNeeded(pathParam = "id")
    public Response delete(@PathParam("id") Long id) {
        System.out.println("DELETE "+id);
        Candidate result = candidateRepository.getById(id);
        // if the candidate to delete does not exist
        if (result == null) {   // return a 404
            throw new ServiceException(Response.Status.NOT_FOUND, "Candidate not found for id: " + id);
        }
        
        candidateRepository.removeById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @DELETE
    @Path("{id}/offers")
    @JWTTokenNeeded(pathParam = "id")
    public Response refuseOffers(@PathParam("id") Long id) {
        AssociatedCandidate result = associatedCandidateRepository.getById(id);
        if (result == null) {   // return a 404
            throw new ServiceException(Response.Status.NOT_FOUND, "Offers not found for id: " + id);
        }
        
        associatedCandidateRepository.removeById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
