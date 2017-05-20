package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;

public class CommentControllerTest {

    private Logger logger = Logger.getLogger(getClass().getName());

    // global declaration
    private UsersService userService;
    private BlogsService blogService;
    private CommentsService commentService;

    private User admin;
    private Blog blog;

    @BeforeClass
    public static void init() {
        System.out.println("@BeforeClass - runOnceBeforeClass");
    }

    // Run once, e.g close connection, cleanup
    @AfterClass
    public static void clanup() {
        System.out.println("@AfterClass - runOnceAfterClass");
    }

    // setup function
    @Before
    public void setup() {
        // Instanced the services
        System.out.println("Setup before test method.");
        userService = UsersService.getInstance();
        blogService = BlogsService.getInstance();
        commentService = CommentsService.getInstance();
        // create admin user
        createAdmin();
        // create a new blog
        createBlog(admin, "Cohart Training", "CMAD Blog",
                "Cisco organise the CMAD training of Advance development program.");
        blog = blogService.readAllBlogs(0).get(0);
        logger.info("Setup end.");
    }

    // teardown function
    @After
    public void teardown() {
        logger.info("Teardown after test method.");
        // delete blog
        deleteBlog(blog.getBlogId());
        deleteAdmin();
        logger.info("Teardown end.");
    }

    // create user
    void createAdmin() {
        admin = new User();
        admin.setUserId("admin").setEmailId("admin@techblog.com").setFirstName("admin").setPassword("abc123");
        // call user create service;
        userService.create(admin);
    }

    // delete admin
    void deleteAdmin() {
        userService.delete("admin");
        admin = null;
    }

    // create blog
    void createBlog(User blogger, String category, String title, String blogMsg) {
        try {
            // declare blog entity elements
            Blog blog = new Blog();
            // set entity value
            blog.setAuthor(blogger).setCategory(category).setTitle(title).setBlogText(blogMsg);

            // call blog create service
            blogService.create(blog);

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

    // delete blog
    void deleteBlog(long blogId) {
        blogService.delete(blogId);
        blog = null;
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

    // update comment
    void updateComment(Comment updateComment, String updateMsg) {
        try {
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
    void deleteComment(long commentId) {
        commentService.delete(commentId);
    }

    // validate comment
    void validateComment(Comment lComment, String blogger, String blogTitle, String commentText) {
        assert (blogger.compareTo(lComment.getAddedBy().getUserId()) == 0);
        assert (blogTitle.compareTo(lComment.getBlog().getTitle()) == 0);
        assert (commentText.compareTo(lComment.getCommentText()) == 0);
    }

    // Test comment creation
    @Test
    public void createComment() {
        try {
            // add a comment ot the blog
            createCommentToBlog(userService.read("admin"), blog, "CMAD is a very good traing program");
            // read the comment
            Comment createdComment = commentService.readAllByBlogId(blog.getBlogId(), 0).get(0);
            // validate the comment
            validateComment(createdComment, "admin", "CMAD Blog", "CMAD is a very good traing program");

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
    public void readComment() {
        try {
            // add a comment ot the blog
            createCommentToBlog(userService.read("admin"), blog, "CMAD is a very good traing program");
            // read the comment
            Comment createdComment = commentService.readAllByBlogId(blog.getBlogId(), 0).get(0);
            // validate the comment fields
            // assert (createdComment.getCommentText() == "CMAD is a very good
            // traing program");
            assert ("CMAD is a very good traing program".compareTo(createdComment.getCommentText()) == 0);
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
    public void updateComment() {
        try {
            // add a comment ot the blog
            createCommentToBlog(userService.read("admin"), blog, "CMAD is a very good traing program");
            // read the comment
            Comment createdComment = commentService.readAllByBlogId(blog.getBlogId(), 0).get(0);
            // validate the comment
            validateComment(createdComment, "admin", "CMAD Blog", "CMAD is a very good traing program");
            // update comment
            updateComment(createdComment, "CMAD is a very good traing program. We have enroled for it.");
            // read the comment
            createdComment = commentService.readAllByBlogId(blog.getBlogId(), 0).get(0);
            // validate updated comment
            validateComment(createdComment, "admin", "CMAD Blog",
                    "CMAD is a very good traing program. We have enroled for it.");
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
    public void deleteComment() {
        try {
            // add a comment ot the blog
            createCommentToBlog(userService.read("admin"), blog, "CMAD is a very good traing program");
            // read the comment
            Comment createdComment = commentService.readAllByBlogId(blog.getBlogId(), 0).get(0);
            // validate the comment
            validateComment(createdComment, "admin", "CMAD Blog", "CMAD is a very good traing program");
            // delete comment
            long commentId = createdComment.getCommentId();
            deleteComment(commentId);

            try {
                // Validate the comment existence
                commentService.read(commentId);
            } catch (DataNotFoundException e) {
                // Do nothing. Expected this exception
            }
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
