package com.core.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomErrorHandler implements ExceptionMapper<BaseCustomError> {

    @Override
    public Response toResponse(BaseCustomError exception) {
        return Response.status(exception.statusCode).entity(exception.getMessage()).build();
    }
}