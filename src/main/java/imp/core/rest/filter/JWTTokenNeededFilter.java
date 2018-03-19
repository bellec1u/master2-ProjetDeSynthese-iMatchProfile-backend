/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.filter;

import imp.core.bean.authentication.AuthManager;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
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
    
    @Context
    private ResourceInfo resourceInfo;

 
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
 
        // Check if token is valid
        Claims claims = authManager.validateToken(token);
        if (claims == null) {
            requestContext.abortWith(generateAbortResponse("Invalid or expired token"));
            return;
        }
        
        // Check if the id in the token is the same as the id in the path
        String pathIdKey = getPathIdKey();
        String pathId = getPathId(requestContext, pathIdKey);
        if (pathId != null && !claims.get("id").equals(pathId)) {
            requestContext.abortWith(generateAbortResponse("Authentication is required"));
        }
    }
    
    private Response generateAbortResponse(String message) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \""+message+"\"}").build();
    }
    
    /**
     * Retrieves the user identifier key passed as parameter to JWTTokenNeeded
     * annotation.
     * @return key
     */
    private String getPathIdKey() {
        Method method = resourceInfo.getResourceMethod();

        String key = null;
        if (method != null) {
            JWTTokenNeeded tokenAnnot = method.getAnnotation(JWTTokenNeeded.class);
            if (tokenAnnot != null) {
                key = tokenAnnot.pathParam();
                if (key.isEmpty()) {
                    return null;
                }
                //System.out.println("KEY : " + key);
            }
            
        }
        return key;
    }
    
    /**
     * Searches the value corresponding to key in the request path pattern.
     * If the pattern is "exemples/{id}", the key is "id" and the request path
     * is "exemples/12", then the value returned will be "12".
     * @param requestContext
     * @param key
     * @return value
     */
    private String getPathId(ContainerRequestContext requestContext, String key) {
        if (key == null) {
            return null;
        }
        String pathId = requestContext.getUriInfo().getPathParameters().getFirst(key);
        //System.out.println("PATH ID : " + pathId);
        return pathId;
    }
}
