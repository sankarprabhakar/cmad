package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.common.config.AppConfig;

public class BlogInjector {
    // Instantiate UserService, BlogService, CommentService
    private static BlogsService blogService = BlogsService.getInstance();
    private static UsersService userService = UsersService.getInstance();
    private static CommentsService commentService = CommentsService.getInstance();

    // Declare array list of objects
    private List<User> users = new ArrayList<User>();
    private List<Blog> blogs = new ArrayList<Blog>();
    private List<Comment> comments = new ArrayList<Comment>();

    // Set the path for input json file.
    // final String filePath =
    // "/Users/ssuansia/CISCO_PROJECT/temp/blogContents.json";
    final String filePath = "./src/test/java/com/cisco/cmad/blogs/service/blogContents.json";

    // Set logger
    private Logger logger = Logger.getLogger(getClass().getName());

    // create user [This is helpfull for import]
    static void createUser(String userId, String email, String firstName, String LastName, String passWord) {
        User user = new User();
        user.setUserId(userId).setEmailId(email).setFirstName(firstName).setLastName(LastName).setPassword(passWord);
        userService.create(user);
    }

    // fetch all the user list through service
    static List<User> fetchUser() {
        return userService.readAllUsers();
    }

    // delete user
    void deleteUser() {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userService.delete(user.getUserId());
            users.set(i, null);
        }
    }

    // create a blog
    static void createBlog(User blogger, String category, String title, String content) {
        try {
            Blog blog = new Blog(); /// setBlogId(1)
            blog.setAuthor(blogger).setBlogText(content).setTitle(title).setCategory(category);
            blogService.create(blog);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // get all bloglist
    static List<Blog> getAllBlogs(int size) {
        List<Blog> blogs = new ArrayList<Blog>();
        try {
            int pageNum = 0;
            int lastPage = size / AppConfig.MAX_PAGE_SIZE;
            while (pageNum <= lastPage) {
                List<Blog> pageBlogs = blogService.readAllBlogs(pageNum);
                blogs.addAll(pageBlogs);
                pageNum++;
            }
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blogs;
    }

    // delete a blog
    private void deleteBlog(long blogId) {
        blogService.delete(blogId);
    }

    // read and parse the json file
    private void readJson() {
        JSONArray blogDetails = null;
        JSONParser parser = new JSONParser();
        try {
            File file = new File(filePath);
            FileReader reader = new FileReader(file);
            blogDetails = (JSONArray) parser.parse(reader);

        } catch (ParseException | IOException e) {
            System.out.println("Exception :: " + e.getMessage());
        }

        for (int c = 0; c < blogDetails.size(); c++) {
            JSONArray array = (JSONArray) ((JSONObject) blogDetails.get(c)).get("bloggers");
            System.out.println("***********");
            for (int c1 = 0; c1 < array.size(); c1++) {
                JSONObject obj = (JSONObject) array.get(c1);
                User tu = new User();

                tu.setUserId((String) obj.get("userId"));
                tu.setFirstName((String) obj.get("firstName"));
                tu.setLastName((String) obj.get("lastName"));
                tu.setEmailId((String) obj.get("email"));
                tu.setPassword((String) obj.get("passwd"));
                // stack users
                users.add(tu);
            }

            array = (JSONArray) ((JSONObject) blogDetails.get(c)).get("blogs");
            System.out.println("***********");
            for (int c1 = 0; c1 < array.size(); c1++) {
                JSONObject obj = (JSONObject) array.get(c1);
                Blog tb = new Blog();

                tb.setCategory((String) obj.get("category"));
                tb.setTitle((String) obj.get("title"));
                tb.setBlogText((String) obj.get("content"));

                // stack blogs
                blogs.add(tb);
            }

            array = (JSONArray) ((JSONObject) blogDetails.get(c)).get("comments");
            System.out.println("***********");
            for (int c1 = 0; c1 < array.size(); c1++) {
                JSONObject obj = (JSONObject) array.get(c1);
                Comment tc = new Comment();

                tc.setCommentText((String) obj.get("msg"));

                // stack comments
                comments.add(tc);
            }
        }
    }

    // create comment
    static void createCommentToBlog(User blogger, Blog blogPost, String commentMsg) {
        try {
            // create comment object
            Comment comment = new Comment();
            // set comment properties
            comment.setAddedBy(blogger);
            comment.setBlog(blogPost);
            comment.setCommentText(commentMsg);
            // call comment create service
            commentService.create(comment);

        } catch (InvalidEntityException iee) {
            fail();
        } catch (DuplicateEntityException dee) {
            fail();
        } catch (EntityException ee) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    // Main function
    public static void main(String[] args) {
        int NUM_OF_USERS, NUM_OF_BLOGS, NUM_OF_COMMENTS;

        BlogInjector bobj = new BlogInjector();
        bobj.readJson();

        // get total count of each element
        NUM_OF_USERS = bobj.users.size();
        NUM_OF_BLOGS = bobj.blogs.size();
        NUM_OF_COMMENTS = bobj.comments.size();

        // create multiple users
        for (int index = 0; index < NUM_OF_USERS; index++) {
            User usr = bobj.users.get(index);
            BlogInjector.createUser(usr.getUserId(), usr.getEmailId(), usr.getFirstName(), usr.getLastName(),
                    usr.getPassword());
        }

        // reall all the users
        List<User> usersList = BlogInjector.fetchUser();

        // create multiple blogs
        for (int index = 0; index < NUM_OF_BLOGS; index++) {
            Blog blog = bobj.blogs.get(index);
            int userIndex = index % NUM_OF_USERS;
            User blogger = usersList.get(userIndex);
            System.out.println(blogger.getFirstName());
            // BlogInjector.blogService.create(blog);
            BlogInjector.createBlog(blogger, blog.getCategory(), blog.getTitle(), blog.getBlogText());
        }

        List<Blog> blogs = BlogInjector.getAllBlogs(NUM_OF_BLOGS);
        // create multiple comments
        for (int index = 0; index < NUM_OF_BLOGS; index++) {
            Blog blog = blogs.get(index);
            // run through comments loop
            int commentIndex = 0;
            while (commentIndex < NUM_OF_COMMENTS) {
                Comment comment = bobj.comments.get(commentIndex);
                BlogInjector.createCommentToBlog(blog.getAuthor(), blog, comment.getCommentText());
                commentIndex++;
            }
        }
    }

}
