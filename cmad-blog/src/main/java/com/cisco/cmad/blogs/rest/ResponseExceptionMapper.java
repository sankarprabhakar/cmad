package com.cisco.cmad.blogs.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.cisco.cmad.blogs.api.DataNotFoundException;

@Provider
public class ResponseExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable t) {
        t.printStackTrace();
        if (t instanceof DataNotFoundException)
            return Response.status(404).build();
        else
            return Response.status(500).build();
    }

}
