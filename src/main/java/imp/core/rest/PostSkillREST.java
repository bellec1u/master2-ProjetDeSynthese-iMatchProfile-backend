/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.PostSkillRepository;
import imp.core.entity.Post;
import imp.core.entity.PostSkill;
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
 * @author dyasar
 */
@Stateless
@Path("postskills")
public class PostSkillREST {
    
    @EJB
    private PostSkillRepository postRepo;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<PostSkill> list = postRepo.getAll();
        // because ok() method expects an Entity as parameter
        GenericEntity<List<PostSkill>> gen = new GenericEntity<List<PostSkill>>(list) {};
        return Response.ok(gen).build();
    }
    
   @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsById(@PathParam("id") Long id) {
        PostSkill result = postRepo.getById(id);
        return Response.ok(result).build();

    }
    
}
