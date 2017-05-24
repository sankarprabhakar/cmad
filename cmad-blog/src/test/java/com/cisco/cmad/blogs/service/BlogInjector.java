package com.cisco.cmad.blogs.service;

import java.io.FileReader;

//import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Logger;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BlogInjector {
	
    private static BlogsService blogService;
    private static UsersService userService;
    private List<User> users;
    private List<Blog> blogs;

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
    
    @SuppressWarnings("unchecked")
	void blogContentjsonParsor(){
    	final String filePath = "blogContents.json";
    	JSONArray values = null;
    	
    	Hashtable<String,Hashtable[]> finalMap = new Hashtable<String,Hashtable[]>();  
    	
		try {
			// read the json file
			FileReader reader = new FileReader(filePath);
			
			// set parser
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			// read the users
			values= (JSONArray) jsonObject.get("bloggers");
			
			// update users list
			for(int i=0; i < values.size(); i++){
				User tu = null;
				hashMap = (Hashtable<String, String>) values.get(i);
				
				tu.setId(hashMap.get("userId"));
				tu.setFirstName(hashMap.get("firstName"));
				tu.setLastName(hashMap.get("lastName"));
				tu.setEmailId(hashMap.get("email"));
				tu.setPassword(hashMap.get("passwd"));
				
				users.set(i, tu);
				
			}
			// read the blogs
			values= (JSONArray) jsonObject.get("blogs");
			// update blogs list
			for(int i=0; i<values.size(); i++){
				Blog tu = null;
				hashMap = (Hashtable<String, String>) values.get(i);

				
				
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return values;

    }
    
	public static void main(String[] args) {
		int NUM_OF_USER = 5 ;
		int NUM_OF_BLOG = 25;
		int USER_NUMBER = 0;
		List<User> users = fetchUser();
		User user;
		
		String category ;
		String title ;
		String content ;
		
		// create number of users
		for (int i = 0; i < NUM_OF_USER; i++){
			createUser(userId, email, firstName, LastName, passWord);
		}
		for (int i = 0; i < NUM_OF_BLOG; i++){
			if (i % NUM_OF_USER == 0){
				user = users.get(USER_NUMBER);
				USER_NUMBER += 1;
			}
			createBlog(user, category, title, content);
		}

	}

}
