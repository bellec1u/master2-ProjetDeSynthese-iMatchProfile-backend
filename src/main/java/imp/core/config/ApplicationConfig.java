/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author auktis
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    
    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(imp.core.config.CorsFilter.class);
        resources.add(imp.core.rest.AuthenticateREST.class);
        resources.add(imp.core.rest.CandidateREST.class);
        resources.add(imp.core.rest.ConversationREST.class);
        resources.add(imp.core.rest.ExempleREST.class);
        resources.add(imp.core.rest.PostREST.class);
        resources.add(imp.core.rest.RecruiterREST.class);
        resources.add(imp.core.rest.SkillREST.class);
        resources.add(imp.core.rest.exception.mapper.DebugExceptionMapper.class);
        resources.add(imp.core.rest.exception.mapper.RestValidationExceptionMapper.class);
        resources.add(imp.core.rest.filter.JWTTokenNeededFilter.class);
        resources.add(service.CandidateFacadeREST.class);
        resources.add(service.EducationFacadeREST.class);
        resources.add(service.MatchingFacadeREST.class);
        resources.add(service.PostFacadeREST.class);
        resources.add(service.PostSkillFacadeREST.class);
        resources.add(service.SkillFacadeREST.class);
        resources.add(service.UserFacadeREST.class);
    }
    
}
