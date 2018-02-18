/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest;

import imp.core.bean.authentication.AuthManager;
import imp.core.entity.user.AccessData;
import imp.core.entity.user.Credentials;
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
            throw new ServiceException(Response.Status.UNAUTHORIZED, "User not found");
        }
        
        return Response.ok(ad).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPost(Credentials creds)
    {
        System.out.println(creds.getEmail() + " " + creds.getPassword());
        AccessData ad = authManager.login(creds.getEmail(), creds.getPassword());
        if (ad == null) {
            throw new ServiceException(Response.Status.UNAUTHORIZED, "User not found");
        }
        
        return Response.ok(ad).build();
    }

}
