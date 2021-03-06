/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.ApplyRepository;
import imp.core.bean.AssociatedCandidateRepository;
import imp.core.bean.CandidateRepository;
import imp.core.bean.MatchingRepository;
import imp.core.bean.PostRepository;
import imp.core.entity.post.Apply;
import imp.core.entity.post.AssociatedCandidate;
import imp.core.entity.post.Post;
import imp.core.entity.user.Candidate;
import imp.core.entity.user.Recruiter;
import imp.core.rest.exception.ServiceException;
import static java.lang.Long.parseLong;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ejb.*;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Leopold
 */
@Stateless
@Path("posts")
public class PostREST {

    @EJB
    private PostRepository postRepository;
    
    @EJB
    private MatchingRepository matchingRepository;
    
    @EJB
    private AssociatedCandidateRepository associatedCandidateRepository;
    
    @EJB
    private CandidateRepository candidateRepository;
    
    @EJB
    private ApplyRepository applyRepository;
 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@DefaultValue("-1") @QueryParam("limit") int limit) {
        System.out.println("imp.core.rest.PostREST.getAll()");
        List<Post> list;
        if (limit >= 0) {
            list = postRepository.getAll(limit);
        }
        else {
            list = postRepository.getAll();
        }
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
        if (result == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Post not found for id: " + id);
        }
        return Response.ok(result).build();        
    }
    
    @GET
    @Path("{id}/matchings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMatchings(@PathParam("id") Long id) throws ParseException {
        System.out.println("imp.core.rest.PostREST.getBySkills()");
        JSONArray json = matchingRepository.getByPostId(id);
        
        return Response.ok(json.toJSONString()).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @Valid Post json) {
        System.out.println("imp.core.rest.PostREST.updatePost()");
        Post result = postRepository.getById(id);
        if (result == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Post not found for id: " + id);
        }
        result = postRepository.edit(id, json);
        return Response.ok(result).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        System.out.println("imp.core.rest.PostREST.deletePost()");
        Post result = postRepository.getById(id);
        if (result == null) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Post not found for id: " + id);
        }
        postRepository.removeById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
   
    @GET
    @Path("{id}/candidateByMandatorySkills")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCandidateByMandatorySkills(@PathParam("id") Long postId) {
        System.out.println("imp.core.rest.PostREST.getCandidateByMandatorySkills()");
 
        List<Candidate> list = postRepository.getCandidatebyMandatorySkills(postId);
        if (list == null) {
            list = new ArrayList<>();
        }
        GenericEntity<List<Candidate>> candidates = new GenericEntity<List<Candidate>>(list) {};


        return Response.ok(candidates).build();  
    }

    @GET
    // post id
    @Path("{id}/associatedCandidate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssociatedCandidate(@PathParam("id") Long postId) {
        System.out.println("imp.core.rest.PostREST.getAssociatedCandidate()");
 
        List<AssociatedCandidate> list = associatedCandidateRepository.getAssociatedCandidates(postId);
        GenericEntity<List<AssociatedCandidate>> associatedCandidates = new GenericEntity<List<AssociatedCandidate>>(list) {};


        return Response.ok(associatedCandidates).build();  
    }
    
    @GET
    @Path("{idPost}/isMyPost")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isMyPost(@PathParam("idPost") Long postId) {
        System.out.println("imp.core.rest.PostREST.isMyPost()");
 
        Recruiter r = postRepository.isMyPost(postId);
       
        return Response.ok(r).build();  
    }
        
    @POST
    @Path("{id}/associatedOneCandidate")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response addAssociatedCandidate(@PathParam("id") Long postId,String c) {
        System.out.println("imp.core.rest.PostREST.addAssociatedCandidate()");
        Long candidateId = parseLong(c);
         if (associatedCandidateRepository.exist(postId,candidateId)) {
             System.out.println("ouiiiiiiii");
            throw new ServiceException(Response.Status.NOT_FOUND, "Ce candidat est déjà associé à ce poste");
        }
        AssociatedCandidate a = new AssociatedCandidate(postRepository.getById(postId),candidateRepository.getById(candidateId));
        AssociatedCandidate result = associatedCandidateRepository.create(a);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }
    
 
    @POST
    @Path("{id}/apply")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response apply(@PathParam("id") Long postId, @Valid String candidateId) {
        Long id = parseLong(candidateId);
        System.out.println("imp.core.rest.PostREST.apply()");
        if (applyRepository.exist(postId,id)) {
            throw new ServiceException(Response.Status.FOUND, "You have already applied to this post !");
        }
        Apply a = new Apply(postRepository.getById(postId),candidateRepository.getById(id));
        Apply result = applyRepository.create(a);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    
    @GET
    @Path("{idPost}/apply")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyPostApply(@PathParam("idPost") Long postId) {
        System.out.println("imp.core.rest.PostREST.getMyPostApply()");
         List<Apply> a = applyRepository.getByPost(postId);
        GenericEntity<List<Apply>> applyCandidat = new GenericEntity<List<Apply>>(a) {};

       
        return Response.ok(applyCandidat).build();  
    }
}