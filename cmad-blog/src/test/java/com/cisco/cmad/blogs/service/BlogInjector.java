package com.cisco.cmad.blogs.service;

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
import com.cisco.cmad.blogs.api.User;

public class BlogInjector {
	
    private static BlogsService blogService = BlogsService.getInstance();
    private static UsersService userService = UsersService.getInstance();
    private List<User> users = new ArrayList<User>();
//    final String filePath = "/Users/ssuansia/CISCO_PROJECT/temp/blogContents.json";
    final String filePath = "/Users/ssuansia/SOUMYA_PROJECTS/CMAD-4/github_cmad4/cmad/cmad-blog/src/test/java/com/cisco/cmad/blogs/service/blogContents.json";
    private List<Blog> blogs = new ArrayList<Blog>();;
    

    private Logger logger = Logger.getLogger(getClass().getName());

    static void createUser(String userId, String email, String firstName, String LastName, String passWord){

        User user = new User();
        user.setUserId(userId).setEmailId(email).setFirstName(firstName).setLastName(LastName)
                .setPassword(passWord);
        userService.create(user);
    }

    static List<User> fetchUser (){
    	return userService.readAllUsers();
    }
    
    void deleteUser() {
    	for (int i = 0; i < users.size(); i++ ){
    		User user = users.get(i);
    		userService.delete(user.getUserId());
    		users.set(i, null);
    	}
    }

    static void createBlog(User blogger, String category, String title, String content ) {
        try {
            Blog blog = new Blog(); /// setBlogId(1)
            blog.setAuthor(blogger).setBlogText(title).setTitle(content).setCategory(category);
            blogService.create(blog);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    private void deleteBlog(long blogId) {
        blogService.delete(blogId);
    }
    
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
	    		 
	    		 // stack users
	    		 blogs.add(tb); 
	    	 }
        }
    }
    
	public static void main(String[] args) {
		int NUM_OF_USER, NUM_OF_BLOG , USER_NUMBER;
		
		BlogInjector bobj = new BlogInjector();
		bobj.readJson();

		NUM_OF_USER = bobj.users.size();
		NUM_OF_BLOG = bobj.blogs.size();
		
		for (int c = 0; c < NUM_OF_USER ; c++){
			User usr = bobj.users.get(c);
			bobj.createUser(usr.getUserId(), usr.getEmailId(), usr.getFirstName(), usr.getLastName(), usr.getPassword());
		}
		
		List<User> usersList = bobj.fetchUser();
		
		for (int c = 0; c < NUM_OF_BLOG; c++){
			Blog blog = bobj.blogs.get(c);
			blog.setAuthor(usersList.get(c / NUM_OF_USER));
			System.out.println(usersList.get(c / NUM_OF_USER).getFirstName());
			bobj.blogService.create(blog);
		}
	}

}
