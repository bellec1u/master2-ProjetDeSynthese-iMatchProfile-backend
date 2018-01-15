/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.SkillRepository;
import imp.core.entity.Skill;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Mohamed
 */
@Stateless
@Path("skills")
public class SkillREST {
    
    @EJB
    private SkillRepository skillRepository;
    
    /**
     * Returns all the skills.
     *
     * @param description The description of the skills search.
     * @return A response containing all the candidates.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("description") String description) {
        List<Skill> list;
        if (description != null) {
            list = skillRepository.getLikeDescription(description);
        }
        else {
            list = skillRepository.getAll();
        }
        GenericEntity<List<Skill>> skills = new GenericEntity<List<Skill>>(list) {
        };
        return Response
                .ok(skills)
                .build();
    }
    
}
