package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

//import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;

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
//    final String filePath = "/Users/ssuansia/CISCO_PROJECT/temp/blogContents.json";
    final String filePath = "/Users/ssuansia/SOUMYA_PROJECTS/CMAD-4/github_cmad4/cmad/cmad-blog/src/test/java/com/cisco/cmad/blogs/service/blogContents.json";
    
    
    // Set logger 
    private Logger logger = Logger.getLogger(getClass().getName());
    
    // create user [This is helpfull for import]
    static void createUser(String userId, String email, String firstName, String LastName, String passWord){

        User user = new User();
        user.setUserId(userId).setEmailId(email).setFirstName(firstName).setLastName(LastName)
                .setPassword(passWord);
        userService.create(user);
    }

    // fetch all the user list through service
    static List<User> fetchUser (){
    	return userService.readAllUsers();
    }
    
    // delete user
    void deleteUser() {
    	for (int i = 0; i < users.size(); i++ ){
    		User user = users.get(i);
    		userService.delete(user.getUserId());
    		users.set(i, null);
    	}
    }

    // create a blog
    static void createBlog(User blogger, String category, String title, String content ) {
        try {
            Blog blog = new Blog(); /// setBlogId(1)
            blog.setAuthor(blogger).setBlogText(title).setTitle(content).setCategory(category);
            blogService.create(blog);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    // delete a blog
    private void deleteBlog(long blogId) {
        blogService.delete(blogId);
    }
    
    // read and parse the json file
    private void readJson()  {
        JSONArray blogDetails = null;
        JSONParser parser = new JSONParser();
        try {
            File file = new File(filePath);
            FileReader reader = new FileReader(file);
            blogDetails = (JSONArray) parser.parse(reader);

        } catch (ParseException | IOException e) {
        	System.out.println("Exception :: " + e.getMessage());
        } 

        for(int c = 0 ; c < blogDetails.size() ; c++){
        	JSONArray array  = (JSONArray)((JSONObject) blogDetails.get(c)).get("bloggers");
        	System.out.println("***********");
	    	 for(int c1 = 0; c1 < array.size() ; c1++){
	    		 JSONObject obj = (JSONObject)array.get(c1);
	    		 User tu = new User();
	    		 
	    		 tu.setUserId((String)obj.get("userId"));
	    		 tu.setFirstName((String)obj.get("firstName"));
	    		 tu.setLastName((String)obj.get("lastName"));
	    		 tu.setEmailId((String)obj.get("email"));
	    		 tu.setPassword((String)obj.get("passwd"));
	    		 // stack users
	    		 users.add(tu); 
	    	 }
	    	 
	    	array  = (JSONArray)((JSONObject) blogDetails.get(c)).get("blogs");
        	System.out.println("***********");
	    	 for(int c1 = 0; c1 < array.size() ; c1++){
	    		 JSONObject obj = (JSONObject)array.get(c1);
	    		 Blog tb = new Blog();
	    		 
	    		 tb.setCategory((String) obj.get("category"));
	    		 tb.setTitle((String) obj.get("title"));
	    		 tb.setBlogText((String) obj.get("content"));
	    		 
	    		 // stack blogs
	    		 blogs.add(tb); 
	    	 }
	    	 
	    	 array  = (JSONArray)((JSONObject) blogDetails.get(c)).get("comments");
	        	System.out.println("***********");
		    	 for(int c1 = 0; c1 < array.size() ; c1++){
		    		 JSONObject obj = (JSONObject)array.get(c1);
		    		 Comment tc = new Comment();
		    		 
		    		 tc.setCommentText((String) obj.get("msg"));
		    		 
		    		 // stack comments
		    		 comments.add(tc); 
		    	 }
        }
    }
    
    // create comment
    void createCommentToBlog(User blogger, Blog blogPost, String commentMsg) {
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
		int NUM_OF_USER, NUM_OF_BLOG , NUM_OF_COMMENTS ;
		
		BlogInjector bobj = new BlogInjector();
		bobj.readJson();

		// get total count of each element
		NUM_OF_USER = bobj.users.size();
		NUM_OF_BLOG = bobj.blogs.size();
		NUM_OF_COMMENTS = bobj.comments.size();
		
		// create multiple users
		for (int c = 0; c < NUM_OF_USER ; c++){
			User usr = bobj.users.get(c);
			bobj.createUser(usr.getUserId(), usr.getEmailId(), usr.getFirstName(), usr.getLastName(), usr.getPassword());
		}
		
		// reall all the users
		List<User> usersList = bobj.fetchUser();
		
		// create multiple blogs
		for (int c = 0; c < NUM_OF_BLOG; c++){
			Blog blog = bobj.blogs.get(c);
			blog.setAuthor(usersList.get(c / NUM_OF_USER));
			System.out.println(usersList.get(c / NUM_OF_USER).getFirstName());
			bobj.blogService.create(blog);
		}
		
		// create multiple comments
		for (int c = 0; c < NUM_OF_BLOG; c++){ 		// loop through blogs
			Blog blog = bobj.blogs.get(c);
			// run through comments loop
			int c1 = 0;
			while (c1 < (NUM_OF_BLOG % NUM_OF_COMMENTS)){
				Comment comment = bobj.comments.get(c1);
				comment.setBlog(blog);
				
				// select user index
				comment.setAddedBy(usersList.get(c1 % NUM_OF_USER));	
				
				// post a comment
				bobj.commentService.create(comment);
				// increment counter for comment
				c1++;
			}
		}
	}

}
