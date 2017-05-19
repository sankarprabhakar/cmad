package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;

public class BlogsControllerTest {
    private BlogsService blogService;
    private UsersService userService;
    private User admin;

    @BeforeClass
    public static void init() {
        System.out.println("@BeforeClass - runOnceBeforeClass");
    }

    // Run once, e.g close connection, cleanup
    @AfterClass
    public static void clanup() {
        System.out.println("@AfterClass - runOnceAfterClass");
    }

    // Should rename to @BeforeTestMethod
    // e.g. Creating an similar object and share for all @Test
    @Before
    public void setup() {
        userService = UsersService.getInstance();
        blogService = BlogsService.getInstance();
        createAdmin();
        System.out.println("@Before - runBeforeTestMethod");
    }

    // Should rename to @AfterTestMethod
    @After
    public void teardown() {
        deleteAdmin();
        System.out.println("@After - runAfterTestMethod");
    }

    void createAdmin() {
        admin = new User();
        admin.setUserId("admin").setEmailId("admin@tecblog.com").setFirstName("admin").setLastName("")
                .setPassword("xyz");
        userService.create(admin);
    }

    void deleteAdmin() {
        userService.delete("admin");
        admin = null;
    }

    private void validateUser(User user, String userId, String emailId, String firstName, String lastName) {
        assert (user.getUserId().compareTo(userId) == 0);
        assert (user.getEmailId().compareTo(emailId) == 0);
        assert (user.getFirstName().compareTo(firstName) == 0);
        assert (user.getLastName().compareTo(lastName) == 0);
    }

    void createSampleBlog(int index) {
        try {
            Blog blog = new Blog(); /// setBlogId(1)
            blog.setAuthor(admin).setBlogText("Blog Text " + index).setCategory("WEB" + index)
                    .setTitle("Blog Title " + index);
            blogService.create(blog);
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

    private void validateBlog(Blog blog, String userId, String title, String category, String text) {
        assert (userId.compareTo(blog.getAuthor().getUserId()) == 0);
        assert (title.compareTo(blog.getTitle()) == 0);
        assert (category.compareTo(blog.getCategory()) == 0);
        assert (text.compareTo(blog.getBlogText()) == 0);
    }

    private void deleteSampleBlog(long blogId) {
        blogService.delete(blogId);
    }

    private void updateSampleBlog(Blog blog) {
        blogService.update(blog);
    }

    @Test
    public void createBlog() {
        try {
            createSampleBlog(1);
            Blog createdBlog = blogService.readAllBlogs().get(0);
            validateBlog(createdBlog, "admin", "Blog Title 1", "WEB1", "Blog Text 1");

            User user = userService.read("admin");
            validateUser(user, "admin", "admin@tecblog.com", "admin", "");

            deleteSampleBlog(createdBlog.getBlogId());
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

    @Test
    public void readBlog() {
        try {
            List<Blog> blogs;
            Blog blog;

            createSampleBlog(1);
            blog = blogService.readByCategory("WEB1").get(0);
            validateBlog(blog, "admin", "Blog Title 1", "WEB1", "Blog Text 1");

            blogs = blogService.readAllBlogs();
            blog = blogs.get(0);
            validateBlog(blog, "admin", "Blog Title 1", "WEB1", "Blog Text 1");

            // insert delay
            Thread.sleep(1000L);

            createSampleBlog(2);
            blogs = blogService.readByCategory("WEB2");
            assert (blogs.size() == 1);
            blog = blogs.get(0);
            validateBlog(blog, "admin", "Blog Title 2", "WEB2", "Blog Text 2");

            blogs = blogService.readAllBlogs();
            assert (blogs.size() == 2);
            blog = blogService.readAllBlogs().get(1);
            validateBlog(blog, "admin", "Blog Title 1", "WEB1", "Blog Text 1");

            User user = userService.read("admin");
            validateUser(user, "admin", "admin@tecblog.com", "admin", "");

            deleteSampleBlog(blogs.get(0).getBlogId());
            deleteSampleBlog(blogs.get(1).getBlogId());
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

    @Test
    public void updateBlog() {
        try {
            createSampleBlog(1);
            Blog blog = blogService.readAllBlogs().get(0);
            validateBlog(blog, "admin", "Blog Title 1", "WEB1", "Blog Text 1");

            blog.setTitle("Updated Blog Title 1").setCategory("UWEB1").setBlogText("Updated Blog Text 1");
            updateSampleBlog(blog);
            validateBlog(blog, "admin", "Updated Blog Title 1", "UWEB1", "Updated Blog Text 1");

            User user = userService.read("admin");
            validateUser(user, "admin", "admin@tecblog.com", "admin", "");

            deleteSampleBlog(blog.getBlogId());
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

    @Test
    public void deleteBlog() {
        try {
            createSampleBlog(1);
            Blog blog = blogService.readAllBlogs().get(0);

            deleteSampleBlog(blog.getBlogId());

            User user = userService.read("admin");
            validateUser(user, "admin", "admin@tecblog.com", "admin", "");
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

}
