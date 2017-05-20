package com.cisco.cmad.blogs.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.api.Comments;
import com.cisco.cmad.blogs.service.CommentsService;

@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentsController {
    private static Comments comments = CommentsService.getInstance();

    @POST
    @Path("/")
    @JwtTokenExpected
    public Response create(Comment comment) {
        comments.create(comment);
        return Response.ok().entity(comment).build();
    }

    @GET
    @Path("/{commentId}")
    public Response read(@PathParam("commentId") long commentId) {
        Comment comment = comments.read(commentId);
        return Response.ok().entity(comment).build();
    }

    @PUT
    @Path("/")
    @JwtTokenExpected
    public Response update(Comment comment) {
        comments.update(comment);
        return Response.ok().entity(comment).build();
    }

    @DELETE
    @Path("/{commentId}")
    @JwtTokenExpected
    public Response delete(@PathParam("commentId") long blogId) {
        comments.delete(blogId);
        return Response.noContent().build();
    }
}
