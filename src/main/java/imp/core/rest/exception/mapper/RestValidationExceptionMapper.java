/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imp.core.rest.exception.mapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper handling exceptions throwed by bean validation. Used to
 * customize all 400 BAD REQUEST responses.
 *
 * @author alexis
 */
@Provider
public class RestValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("{\"messages\":" + prepareMessage(ex) + ",\"status\":400}")
                .type("application/json")
                .build();
    }

    private String prepareMessage(ConstraintViolationException ex) {
        String msg = "[";
        boolean firstLoop = true;
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            msg += !firstLoop ? "," : "";
            firstLoop = false;
            msg += "\"" + cv.getMessage() + "\"";
        }
        return msg + "]"  ;
    }
}
