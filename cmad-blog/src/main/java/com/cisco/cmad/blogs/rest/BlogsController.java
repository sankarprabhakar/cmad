package com.cisco.cmad.blogs.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.Blogs;
import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.api.Comments;
import com.cisco.cmad.blogs.api.Users;
import com.cisco.cmad.blogs.service.BlogsService;
import com.cisco.cmad.blogs.service.CommentsService;
import com.cisco.cmad.blogs.service.UsersService;

@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogsController {
    private Blogs blogsService = BlogsService.getInstance();
    private Comments commentsService = CommentsService.getInstance();
    private Users usersService = UsersService.getInstance();

    @POST
    @Path("/")
    // @JwtTokenExpected
    public Response create(Blog blog) {
        blogsService.create(blog);
        return Response.ok().entity(blog).build();
    }

    @GET
    @Path("/{blogId}")
    public Response read(@PathParam("blogId") long blogId) {
        Blog blog = blogsService.read(blogId);
        return Response.ok().entity(blog).build();
    }

    @GET
    @Path("/")
    public Response read(@Context UriInfo info) {
        // @QueryParam("category") String category) {
        List<Blog> matched;
        GenericEntity<List<Blog>> entities;
        String category = info.getQueryParameters().getFirst("category");
        matched = blogsService.readByCategory(category);
        entities = new GenericEntity<List<Blog>>(matched) {
        };
        return Response.ok().entity(entities).build();
    }

    @GET
    @Path("/{blogId}/comments")
    public Response readAllComments(@PathParam("blogId") long blogId) {
        List<Comment> comments;
        GenericEntity<List<Comment>> entities;
        comments = commentsService.readAllByBlogId(blogId);
        // Blog blog = blogsService.read(blogId);
        // User user = usersService.read(blog.getAuthor().getUserId());
        // for (int i = 0; i < comments.size(); i++) {
        // Comment comment = comments.get(i);
        // // User user = usersService.read(comment.getAddedBy().getUserId());
        // comment.setAddedBy(user);
        // // Blog blog = blogsService.read(comment.getBlog().getBlogId());
        // blog.setAuthor(user);
        // comment.setBlog(blog);
        // comments.set(i, comment);
        // }
        entities = new GenericEntity<List<Comment>>(comments) {
        };
        return Response.ok().entity(entities).build();
    }

    @PUT
    @Path("/")
    // @JwtTokenExpected
    public Response update(Blog blog) {
        blogsService.update(blog);
        blog = blogsService.read(blog.getBlogId());
        return Response.ok().entity(blog).build();
    }

    @DELETE
    @Path("/{blogId}")
    // @JwtTokenExpected
    public Response delete(@PathParam("blogId") long blogId) {
        blogsService.delete(blogId);
        return Response.noContent().build();
    }

    // @HEAD
    // @Path("/{blogId}")
    // public Response find(@PathParam("blogId") long blogId) {
    // blogsService.read(blogId);
    // return Response.ok().build();
    // }
}