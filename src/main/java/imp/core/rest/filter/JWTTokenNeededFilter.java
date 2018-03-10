/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.filter;

import imp.core.bean.authentication.AuthManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author master2-lmfi
 */
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    @EJB
    private AuthManager authManager;
 
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {        
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            requestContext.abortWith(generateAbortResponse("No authorization header"));
            return;
        }
 
        // Extract the token from the HTTP Authorization header
        if (authorizationHeader.length() <= "Bearer ".length()) {
            requestContext.abortWith(generateAbortResponse("No token provided"));
            return;
        }
        String token = authorizationHeader.substring("Bearer".length()).trim();
 
        
        if (!authManager.isTokenValid(token)) {
            requestContext.abortWith(generateAbortResponse("Invalid or expired token"));
        } 
    }
    
    private Response generateAbortResponse(String message) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \""+message+"\"}").build();
    }
}
