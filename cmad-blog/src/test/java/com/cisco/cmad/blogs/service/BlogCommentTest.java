package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.Blogs;
import com.cisco.cmad.blogs.api.Comments;
import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.api.Users;

public class BlogCommentTest {
	// Instanciate services
	Users userService = UsersService.getInstance();
	Blogs blogService = BlogsService.getInstance();
	Comments commentService = CommentsService.getInstance();
	// create user object
	User blogger = new User();
	// Create Date object
	Date date = new Date();
	// create a blog object
	Blog blogOne = new Blog();
	// create a comment object
	Comment commentOne = new Comment();
	@Test
    public void createBlog() {
        // set user parameter
        blogger.setUserId("ranjan");
        blogger.setFirstName("Ranjan");
        blogger.setLastName("");
        blogger.setPassword("abc123");
        blogger.setEmailId("ranjan@tecblog.com");
        
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
        User createdUser = userService.read("ranjan");

        try {
	        // set blog parameter
	        blogOne.setBlogId(2);
	        blogOne.setAuthor(createdUser);
	        blogOne.setCategory("Training");
	        String blogMsg = "This is some text yet to be updated.";
	        blogOne.setBlogText(blogMsg);
	        blogOne.setTitle("Sample Blog");
	        blogOne.setLastUpdatedOn(date);
	        // Write blog into table
	        blogService.create(blogOne);
	        // Read blog post
	        Blog blogread = blogService.read(blogOne.getBlogId());
	        assert (blogread.getBlogId() == 2);
	        assert (blogread.getCategory() == "Training");
	        assert (blogread.getBlogText() == "This is some text yet to be updated.");
	        
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
    public void createBlogComment() {
        userService.create(blogger);            
        // read user
        User createdUser = userService.read("ranjan");

        try {

        	// read a blog
        	Blog readBlog = blogService.read(2);
        	// set comment attributes
        	commentOne.setCommentId(1);
        	commentOne.setAddedBy(createdUser);
        	commentOne.setBlog(readBlog);
        	commentOne.setCommentText("This is a testing comment.");
        	// create comment
        	commentService.create(commentOne);
        	// read comment
        	Comment readComment = commentService.read(1);
        	
        	// Validate comment
        	assert (readComment.getCommentText() == "This is a testing comment.");
        	assert (readComment.getCommentId() == 1);
        	assert (readComment.getAddedBy() == createdUser);
        	assert (readComment.getBlog() == readBlog);
        	
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
    public void updateBlogComment() {

        // read user
        User createdUser = userService.read("ranjan");

        try {

        	// read a blog
        	Blog readBlog = blogService.read(2);
        	// Modify comment
        	commentOne.setCommentText("This is a testing comment. updated more.");
        	// update comment
        	commentService.update(commentOne);
        	// read comment
        	Comment readComment = commentService.read(1);
        	
        	// Validate comment
        	assert (readComment.getCommentText() == "This is a testing comment. updated more.");
        	assert (readComment.getCommentId() == 1);
        	assert (readComment.getAddedBy() == createdUser);
        	assert (readComment.getBlog() == readBlog);
        	
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
    public void deleteBlogComment() {
        try {
        	// delete comment
        	commentService.delete(1);
        	// read comment
        	Comment readComment = commentService.read(1);
        	// Validate comment
        	assert (readComment == null);
        	
        } catch (EntityException ibe){
        	fail();
        } catch (Exception e){
        	e.printStackTrace();
        	fail();
        }
	}
}
