package com.cisco.cmad.blogs.rest;

import javax.persistence.NoResultException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.cisco.cmad.blogs.api.DataNotFoundException;

@Provider
public class ResponseExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable t) {
        t.printStackTrace();
        if (t instanceof DataNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (t instanceof NoResultException) {
            return Response.status(Response.Status.NO_CONTENT).build(); // HTTP204
        } else if (t instanceof IllegalArgumentException || t instanceof NumberFormatException) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (t instanceof NotAuthorizedException) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
