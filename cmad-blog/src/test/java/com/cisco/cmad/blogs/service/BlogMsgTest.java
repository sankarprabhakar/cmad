package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.cisco.cmad.blogs.api.Blogs;
import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.Blog;

import java.util.Date;


import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.api.Users;


public class BlogMsgTest {
	// Instanciate services
	Users userService = UsersService.getInstance();
	Blogs blogService = BlogsService.getInstance();
	
	// create user object
	User blogger = new User();
	// Create Date object
	Date date = new Date();
	// create a blog object
	Blog blogOne = new Blog();

	@Test
    public void createBlog() {
        // set user parameter
        blogger.setUserId("soumya");
        blogger.setFirstName("Soumya");
        blogger.setLastName("Ranjan");
        blogger.setPassword("abc123");
        blogger.setEmailId("soumya@tecblog.com");
        
        try {
        	// create a user
            userService.create(blogger);
            
        } catch (InvalidEntityException ibe) {
            fail();
        } catch (DuplicateEntityException dbe) {
            fail();
        } catch (EntityException le) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        
        // read user
        User createdUser = userService.read("soumya");

        try {
	        // set blog parameter
	        blogOne.setBlogId(1);
	        blogOne.setAuthor(createdUser);
	        blogOne.setCategory("Business");
	        String blogMsg = "This is a make in India sample test message.";
	        blogOne.setBlogText(blogMsg);
	        blogOne.setTitle("Make India");
	        blogOne.setLastUpdatedOn(date);
	        // Write blog into table
	        blogService.create(blogOne);
	        // Read blog post
	        Blog blogread = blogService.read(blogOne.getBlogId());
	        
	        // Validate blog contents
	        assert (blogread.getBlogId() == 1);
	        assert (blogread.getBlogText() == blogOne.getBlogText());
	        assert (blogread.getAuthor() == createdUser);
	        assert (blogread.getCategory() == "Business");
	        assert (blogread.getLastUpdatedOn() == date);
	        
        } catch (InvalidEntityException ibe){
        	fail();
        } catch (DuplicateEntityException dbe){
        	fail();
        } catch (EntityException le){
        	fail();
        } catch (Exception e){
        	e.printStackTrace();
        	fail();
        }
    }
	
	@Test
    public void updateBlog() {

		User createdUser = userService.read("soumya");

        try {
	        // set blog parameter
	        blogOne.setBlogId(1);
	        blogOne.setAuthor(createdUser);
	        blogOne.setCategory("Entrepreneur");
	        String blogMsg = "Make in India is a great initive. Nation first.";
	        blogOne.setBlogText(blogMsg);
	        blogOne.setTitle("Make India");
	        blogOne.setLastUpdatedOn(date);
	        // Write blog into table
	        blogService.create(blogOne);
	        // Read blog post
	        Blog blogread = blogService.read(blogOne.getBlogId());
	        
	        // Validate blog contents
	        assert (blogread.getBlogId() == 1);
	        assert (blogread.getBlogText() == blogMsg);
	        assert (blogread.getCategory() == "Entrepreneur");
	        
        } catch (DataNotFoundException ibe){
        	fail();
        } catch (EntityException dbe){
        	fail();
        } catch (Exception e){
        	e.printStackTrace();
        	fail();
        }
    }

	@Test
    public void deleteBlog() {
		
        try {
	        blogService.delete(1);
	        Blog blogread = blogService.read(blogOne.getBlogId());
	        assert (blogread == null);
	        
        } catch (DataNotFoundException ibe){
        	fail();
        } catch (EntityException dbe){
        	fail();
        } catch (Exception e){
        	e.printStackTrace();
        	fail();
        }
    }


}
