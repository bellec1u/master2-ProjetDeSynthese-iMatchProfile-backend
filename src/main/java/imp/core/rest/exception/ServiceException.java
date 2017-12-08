/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author auktis
 */
public class ServiceException extends WebApplicationException {
    
    private Response.Status status;
    private String message;
    
    
    public ServiceException(Response.Status status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
    
    @Override
    public Response getResponse() {
        CustomError err = new CustomError(status.getStatusCode(), message);
        return Response.status(status).entity(err).type(MediaType.APPLICATION_JSON).build();
    }
    
}
