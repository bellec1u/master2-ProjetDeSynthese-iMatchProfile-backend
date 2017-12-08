/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.config;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author auktis
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {
 
    @Override
    public void filter(ContainerRequestContext requestContext, 
                       ContainerResponseContext responseContext) throws IOException
    {
        final MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add(
          "Access-Control-Allow-Origin", "*");
        headers.add(
          "Access-Control-Allow-Credentials", "true");
        headers.add(
         "Access-Control-Allow-Headers",
         "origin, content-type, accept, authorization");
        headers.add(
          "Access-Control-Allow-Methods", 
          "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
