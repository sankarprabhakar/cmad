# Simple Blogging Site
## Technologies used
1. Java 1.8
2. Java API for RESTful Web Services (JAX-RS) (2.x)
3. Maven build system (3.x)
4. Plant UML for design documentation
5. Java Persistence API (2.x)
6. Jersey Servlet Container (2.x)
7. Moxy - JSON processor (supports both JSON & XML)  => Alternative JACKSON
8. JSON Web Token (JWT) for authentication & API protection


## Features Supported
### Blog-User Features 
1. Sign up
2. Sign off (Profile delete)
3. Sign in
4. Sign out
5. Profile update (Protected by JWT)
6. Admin view (List Users) (Protected by JWT)
7. Profile delete (Sign off) also deletes all the user blogs, the related comments & user comments (Protected by JWT)

### Blog Features
1. Post a new blog (Protected by JWT)
2. Update your existing blog (Protected by JWT)
3. Search blogs by category 
4. Show all blogs 
5. Delete your blog (Protected by JWT)
6. View/Read blog
7. Listing recently added/updated blogs first
8. Deleting a blog will also delete the related comments
9. Pagination support


### User-Comments on Blogs : Features
1. Add a comment (Protected by JWT - TODO)
2. Delete your comment (owner check supported) (Protected by JWT)
3. Update your comment (owner check supported) (Protected by JWT)
4. Pagination support

### Common Features
1. Protection of APIs using JWT
2. Logging support

### Other UI Features
1. Integrated with Bootstrap for styling & Icons

## Architecture

## REST API
### BLOGS
POST   :  /cmad-blog/tecblogs/blogs
GET    :  /cmad-blog/tecblogs/blogs/{blogId}
GET    :  /cmad-blog/tecblogs/blogs/{blogId}/comments?[page=]
GET    :  /cmad-blog/tecblogs/blogs?[page=]&[category=]
GET    :  /cmad-blog/tecblogs/blogs/users/{userId}?[page=]
PUT    :  /cmad-blog/tecblogs/blogs
DELETE :

    @PUT
    @Path("/")
    @JwtTokenExpected
    public Response update(Blog blog) {
        blogsService.update(blog);
        blog = blogsService.read(blog.getBlogId());
        return Response.ok().entity(blog).build();
    }

    @DELETE
    @Path("/{blogId}")
    @JwtTokenExpected
    public Response delete(@PathParam("blogId") long blogId) {
        blogsService.delete(blogId);
        return Response.noContent().build();
    }

## Backend Design 

### Class Diagram

![classes](https://cloud.githubusercontent.com/assets/4160178/26356555/0c41e226-3fea-11e7-98cc-b56bc4d953d6.png)

### Signup (Create User) Sequential Diagram

![create-user](https://cloud.githubusercontent.com/assets/4160178/26357678/b20f5442-3fed-11e7-9a72-5cf4bb00aa97.png)

### Signin (Login) - Sequential Diagram

![login](https://cloud.githubusercontent.com/assets/4160178/26357770/fc4bbf78-3fed-11e7-8327-62f2585d44b8.png)

### Create a BLOG - Sequential Diagram

![request-handling](https://cloud.githubusercontent.com/assets/4160178/26357822/2a702114-3fee-11e7-8981-b1446fb39eb7.png)

### Protecting API using JWT / Authorization - Sequential Diagram

![request-validation](https://cloud.githubusercontent.com/assets/4160178/26357933/867da404-3fee-11e7-899d-8f86431225cd.png)
