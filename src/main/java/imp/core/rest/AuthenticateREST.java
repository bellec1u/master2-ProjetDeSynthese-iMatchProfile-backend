/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.AuthManager;
import imp.core.entity.user.AccessData;
import imp.core.rest.exception.ServiceException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author auktis
 */
@Stateless
@Path("login")
public class AuthenticateREST {
    
    @EJB
    private AuthManager authManager;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login() {
        AccessData ad = authManager.login("john.doe@gmail.com", "candidate");
        if (ad == null) {
            throw new ServiceException(Response.Status.UNAUTHORIZED, "Pas trouvé");
        }
        
        return Response.ok(ad).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPost(
            @FormParam("email") String email,
            @FormParam("password") String password)
    {
        System.out.println(email + " " + password);
        AccessData ad = authManager.login(email, password);
        if (ad == null) {
            throw new ServiceException(Response.Status.UNAUTHORIZED, "Pas trouvé");
        }
        
        return Response.ok(ad).build();
    }

}
