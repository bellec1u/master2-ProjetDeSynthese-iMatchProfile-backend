/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.exception;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author auktis
 */
@ApplicationException
public class ServiceException extends WebApplicationException {
    
    private final Response.Status status;
    private final String message;
    
    
    public ServiceException(Response.Status status, String message) {
        super(status);
        this.status = status;
        this.message = message;
    }
    
    @Override
    public Response getResponse() {
        CustomError err = new CustomError(status.getStatusCode(), message);
        return Response.status(status).entity(err).type(MediaType.APPLICATION_JSON).build();
    }
    
}
