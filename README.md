# Simple Blogging Site
# Technologies used
1. Java 1.8
2. Java API for RESTful Web Services (JAX-RS) (2.x)
3. Maven build system (3.x)
4. Plant UML for design documentation
5. Java Persistence API (2.x)
6. Jersey Servlet Container (2.x)
7. Moxy - JSON processor (supports both JSON & XML)  => Alternative JACKSON
8. JSON Web Token (JWT) for authentication & API protection


# Features Supported
# Blog-User Features 
1. Sign up
2. Sign off (Profile delete)
3. Sign in
4. Sign out
5. Profile update (Protected by JWT)
6. Admin view (List Users) (Protected by JWT)
7. Profile delete (Sign off) also deletes all the user blogs, the related comments & user comments (Protected by JWT)

# Blog Features
1. Post a new blog (Protected by JWT)
2. Update your existing blog (Protected by JWT)
3. Search blogs by category 
4. Show all blogs 
5. Delete your blog (Protected by JWT)
6. View/Read blog
7. Listing recently added/updated blogs first
8. Deleting a blog will also delete the related comments
9. Pagination support


# User-Comments on Blogs : Features
1. Add a comment (Protected by JWT - TODO)
2. Delete your comment (owner check supported) (Protected by JWT)
3. Update your comment (owner check supported) (Protected by JWT)
4. Pagination support

# Common Features
1. Protection of APIs using JWT
2. Logging support

# Other UI Features
1. Integrated with Bootstrap for styling & Icons

# Architecture


# Backend Design 

# Class Diagram

# ![Classes](/ctecblog/cmad/tree/master/cmad-blog/src/main/docs/uml/class.png?raw=true)

 (/ctecblog/cmad/tree/master/cmad-blog/src/main/docs/uml/class.png?raw=true)
