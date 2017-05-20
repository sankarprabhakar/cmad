package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;

public class CommentControllerTest {
	// global declaration
	private UsersService userService;
	private BlogsService blogService;
	private CommentsService commentService;
	
	private User admin;
	private Blog blog;
	
	// setup function
	public void setup(){
		// Instanced the services
		System.out.println("Setup before test method.");
		userService = UsersService.getInstance();
		blogService = BlogsService.getInstance();
		commentService = CommentsService.getInstance();
		// create admin user
		createAdmin();
		System.out.println("Setup end.");
		
	}
	
	// teardown function
	public void teardown(){
		System.out.println("Teardown after test method.");
		deleteAdmin();
		System.out.println("Teardown end.");
	}
	
	//create user
	void createAdmin(){
		admin = new User();
		admin.setUserId("admin");
		admin.setEmailId("admin@techblog.com");
		admin.setFirstName("admin");
		admin.setPassword("abc123");
		// call user create service;
		userService.create(admin);
	}
	
	// delete admin
	void deleteAdmin(){
		userService.delete("admin");
	}

	// create blog
	void createBlog(User blogger, String category, String title, String blogMsg){
		try{
			// declare blog entity elements
			Blog blog = new Blog();
			// set entity value
			blog.setAuthor(blogger);
			blog.setCategory(category);
			blog.setTitle(title);
			blog.setBlogText(blogMsg);
			
			// call blog create service
			blogService.create(blog);
			
		} catch (InvalidEntityException iee){
			fail();
		} catch (DuplicateEntityException dee){
			fail();
		} catch (EntityException ee){
			fail();
		} catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	// delete blog
	void deleteBlog(long blogId){
		blogService.delete(blogId);
	}
	
	// create comment
	void createCommentToBlog(User blogger, Blog blogPost, String commentMsg){
		try{
			// create comment object
			Comment comment = new Comment();
			// set comment properties
			comment.setAddedBy(blogger);
			comment.setBlog(blogPost);
			comment.setCommentText(commentMsg);
			// call comment create service
			commentService.create(comment);
			
		} catch (InvalidEntityException iee){
			fail();
		} catch (DuplicateEntityException dee){
			fail();
		} catch (EntityException ee){
			fail();
		} catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}

	// update comment
	void updateComment(Comment updateComment, String updateMsg){
		try{
			updateComment.setCommentText(updateMsg);
			commentService.update(updateComment);
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
	}
	
	// delete comment
	
	void deleteComment(long commentId){
		commentService.delete(commentId);
	}

	// validate comment
	void validateComment(Comment lComment, String blogger, String blogTitle, String commentText){
		assert (blogger.compareTo(lComment.getAddedBy().getUserId()) == 0);
		assert (blogTitle.compareTo(lComment.getBlog().getTitle()) == 0);
		assert (commentText.compareTo(lComment.getCommentText()) == 0);
	}
	
	// Test comment creation
	@Test
	public void createComment(){
		try {
			// user created by setup
			// create a new blog
			createBlog(userService.read("admin"), "Cohart Training", "CMAD Blog", "Cisco organise the CMAD training of Advance development program.");
			// read created blog
			Blog createdBlog = blogService.readAllBlogs(0).get(0);
			// add a comment ot the blog
			createCommentToBlog(userService.read("admin"), createdBlog, "CMAD is a very good traing program");
			// read the comment
			Comment createdComment = commentService.readAllByBlogId(createdBlog.getBlogId()).get(0);
			// validate the comment
			validateComment(createdComment, "admin", "CMAD Blog", "CMAD is a very good traing program");
			// delete blog
			deleteBlog(createdBlog.getBlogId());
			
		} catch (InvalidEntityException iee) {
            fail();
        } catch (DuplicateEntityException dee) {
            fail();
        } catch (EntityException le) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } 
		// user will be deleted by teardown.
		// once user deleted entity mapping records will be deleted automatic.
	}
	
	@Test
	public void readComment(){
		try {
			// user created by setup
			// create a new blog
			createBlog(userService.read("admin"), "Cohart Training", "CMAD Blog", "Cisco organise the CMAD training of Advance development program.");
			// read created blog
			Blog createdBlog = blogService.readAllBlogs(0).get(0);
			// add a comment ot the blog
			createCommentToBlog(userService.read("admin"), createdBlog, "CMAD is a very good traing program");
			// read the comment
			Comment createdComment = commentService.readAllByBlogId(createdBlog.getBlogId()).get(0);
			// validate the comment fields
			// assert (createdComment.getCommentText() == "CMAD is a very good traing program");
			assert ("CMAD is a very good traing program".compareTo(createdComment.getCommentText()) == 0);
			// delete blog
			deleteBlog(createdBlog.getBlogId());
			
		} catch (InvalidEntityException iee) {
            fail();
        } catch (DuplicateEntityException dee) {
            fail();
        } catch (EntityException le) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } 
		// user will be deleted by teardown.
		// once user deleted entity mapping records will be deleted automatic.
	}
	
	
	@Test
	public void updateComment(){
		try {
			// user created by setup
			// create a new blog
			createBlog(userService.read("admin"), "Cohart Training", "CMAD Blog", "Cisco organise the CMAD training of Advance development program.");
			// read created blog
			Blog createdBlog = blogService.readAllBlogs(0).get(0);
			// add a comment ot the blog
			createCommentToBlog(userService.read("admin"), createdBlog, "CMAD is a very good traing program");
			// read the comment
			Comment createdComment = commentService.readAllByBlogId(createdBlog.getBlogId()).get(0);
			// validate the comment
			validateComment(createdComment, "admin", "CMAD Blog", "CMAD is a very good traing program");
			// update comment
			updateComment(createdComment, "CMAD is a very good traing program. We have enroled for it.");
			// read the comment
			createdComment = commentService.readAllByBlogId(createdBlog.getBlogId()).get(0);
			// validate updated comment
			validateComment(createdComment, "admin", "CMAD Blog", "CMAD is a very good traing program. We have enroled for it.");
			// delete blog
			deleteBlog(createdBlog.getBlogId());
			
		} catch (InvalidEntityException iee) {
            fail();
        } catch (DuplicateEntityException dee) {
            fail();
        } catch (EntityException le) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } 
		// user will be deleted by teardown.
		// once user deleted entity mapping records will be deleted automatic.
	}
	
	@Test
	public void deleteComment(){
		try {
			// user created by setup
			// create a new blog
			createBlog(userService.read("admin"), "Cohart Training", "CMAD Blog", "Cisco organise the CMAD training of Advance development program.");
			// read created blog
			Blog createdBlog = blogService.readAllBlogs(0).get(0);
			// add a comment ot the blog
			createCommentToBlog(userService.read("admin"), createdBlog, "CMAD is a very good traing program");
			// read the comment
			Comment createdComment = commentService.readAllByBlogId(createdBlog.getBlogId()).get(0);
			// validate the comment
			validateComment(createdComment, "admin", "CMAD Blog", "CMAD is a very good traing program");
			// delete comment
			long commentId = createdComment.getCommentId(); 
			deleteComment(commentId);
			// Validate the comment existance
			assert (commentService.read(commentId) == null);
			// delete blog
			deleteBlog(createdBlog.getBlogId());
			
		} catch (InvalidEntityException iee) {
            fail();
        } catch (DuplicateEntityException dee) {
            fail();
        } catch (EntityException le) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } 
		// user will be deleted by teardown.
		// once user deleted entity mapping records will be deleted automatic.
	}
	
}
