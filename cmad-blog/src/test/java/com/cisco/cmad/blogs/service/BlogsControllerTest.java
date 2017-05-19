package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.PaginationUtils;
import com.cisco.cmad.blogs.api.User;

public class BlogsControllerTest {
    private BlogsService blogService;
    private UsersService userService;
    private User admin;

    private Logger logger = Logger.getLogger(getClass().getName());

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

    void createSampleBlog(int index, boolean sameCategory) {
        try {
            Blog blog = new Blog(); /// setBlogId(1)
            blog.setAuthor(admin).setBlogText("Blog Text " + index).setTitle("Blog Title " + index);
            if (sameCategory) {
                blog.setCategory("WEB");
            } else {
                blog.setCategory("WEB" + index);
            }
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
            createSampleBlog(1, false);
            Blog createdBlog = blogService.readAllBlogs(0).get(0);
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

            createSampleBlog(1, false);
            blog = blogService.readByCategory("WEB1", 0).get(0);
            validateBlog(blog, "admin", "Blog Title 1", "WEB1", "Blog Text 1");

            blogs = blogService.readAllBlogs(0);
            blog = blogs.get(0);
            validateBlog(blog, "admin", "Blog Title 1", "WEB1", "Blog Text 1");

            // insert delay
            Thread.sleep(1000L);

            createSampleBlog(2, false);
            blogs = blogService.readByCategory("WEB2", 0);
            assert (blogs.size() == 1);
            blog = blogs.get(0);
            validateBlog(blog, "admin", "Blog Title 2", "WEB2", "Blog Text 2");

            blogs = blogService.readAllBlogs(0);
            assert (blogs.size() == 2);
            blog = blogService.readAllBlogs(0).get(1);
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
            createSampleBlog(1, false);
            Blog blog = blogService.readAllBlogs(0).get(0);
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
            createSampleBlog(1, false);
            Blog blog = blogService.readAllBlogs(0).get(0);

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

    @Test
    public void readBlogPagesByCategory() {
        try {
            // List<Blog> blogs;
            // Blog blog;
            int TOTAL_BLOGS = 17;
            createSampleBlogs(TOTAL_BLOGS);
            navigateForwardByCategory(TOTAL_BLOGS);
            navigateBackwardsByCategory(TOTAL_BLOGS);
            deleteSampleBlogs(TOTAL_BLOGS);
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
    public void readBlogPages() {
        try {
            // List<Blog> blogs;
            // Blog blog;
            int TOTAL_BLOGS = 23;
            createSampleBlogs(TOTAL_BLOGS);
            navigateForward(TOTAL_BLOGS);
            navigateBackwards(TOTAL_BLOGS);
            deleteSampleBlogs(TOTAL_BLOGS);
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

    private void createSampleBlogs(int totalBlogs) {
        for (int i = 0; i < totalBlogs; i++) {
            createSampleBlog(i, true);
            logger.info("CREATE SAMPLE BLOG[" + i + "]");
        }
    }

    private int navigateForwardByCategory(int totalBlogs) throws DataNotFoundException, EntityException {
        int page = 0;
        // Forward pagination
        for (page = 0; page < totalBlogs / PaginationUtils.MAX_PAGE_SIZE; page++) {
            logger.info("PAGE FORWARD" + page);
            int blogsCount = blogService.readByCategory("WEB", page).size();
            logger.info("BLOGS COUNT = " + blogsCount);
            assert (blogsCount == PaginationUtils.MAX_PAGE_SIZE);
        }
        int lastPageCount = totalBlogs % PaginationUtils.MAX_PAGE_SIZE;
        if (lastPageCount > 0) {
            assert (blogService.readByCategory("WEB", page++).size() == lastPageCount);
            logger.info("PAGE FORWARD LASTPAGE " + page);
        }

        try {
            blogService.readByCategory("WEB", page++);
        } catch (Exception e) {
            page--;
            assert (true);
        }

        return page;
    }

    private int navigateBackwardsByCategory(int totalBlogs) throws DataNotFoundException, EntityException {
        int totalPages = totalBlogs / PaginationUtils.MAX_PAGE_SIZE;
        int lastPageCount = totalBlogs % PaginationUtils.MAX_PAGE_SIZE;
        if (lastPageCount > 0) {
            totalPages++;
        }
        int page = totalPages - 1;
        logger.info("PAGE BACKWARD LAST PAGE " + page);
        int blogsCount = blogService.readAllBlogs(page--).size();
        logger.info("BLOGS COUNT = " + blogsCount);
        assert (blogsCount == lastPageCount);

        // Forward pagination
        for (; page >= 0; page--) {
            logger.info("PAGE BACKWARD" + page);
            blogsCount = blogService.readAllBlogs(page).size();
            logger.info("BLOGS COUNT = " + blogsCount);
            assert (blogsCount == PaginationUtils.MAX_PAGE_SIZE);

        }

        try {
            blogService.readByCategory("WEB", page--);
        } catch (Exception e) {
            page++;
            assert (true);
        }

        return page;
    }

    private int navigateForward(int totalBlogs) throws DataNotFoundException, EntityException {
        int page = 0;
        // Forward pagination
        for (page = 0; page < totalBlogs / PaginationUtils.MAX_PAGE_SIZE; page++) {
            logger.info("PAGE FORWARD" + page);
            int blogsCount = blogService.readAllBlogs(page).size();
            logger.info("BLOGS COUNT = " + blogsCount);
            assert (blogsCount == PaginationUtils.MAX_PAGE_SIZE);
        }
        int lastPageCount = totalBlogs % PaginationUtils.MAX_PAGE_SIZE;
        if (lastPageCount > 0) {
            assert (blogService.readAllBlogs(page++).size() == lastPageCount);
            logger.info("PAGE FORWARD LASTPAGE" + page);
        }

        try {
            blogService.readAllBlogs(page++);
        } catch (Exception e) {
            page--;
            assert (true);
        }

        return page;
    }

    private int navigateBackwards(int totalBlogs) throws DataNotFoundException, EntityException {
        int totalPages = totalBlogs / PaginationUtils.MAX_PAGE_SIZE;
        int lastPageCount = totalBlogs % PaginationUtils.MAX_PAGE_SIZE;
        if (lastPageCount > 0) {
            totalPages++;
        }
        int page = totalPages - 1;
        logger.info("PAGE BACKWARD LAST PAGE " + page);
        int blogsCount = blogService.readAllBlogs(page--).size();
        logger.info("BLOGS COUNT = " + blogsCount);
        assert (blogsCount == lastPageCount);

        // Forward pagination
        for (; page >= 0; page--) {
            logger.info("PAGE BACKWARD" + page);
            blogsCount = blogService.readAllBlogs(page).size();
            logger.info("BLOGS COUNT = " + blogsCount);
            assert (blogsCount == PaginationUtils.MAX_PAGE_SIZE);

        }

        try {
            blogService.readAllBlogs(page--);
        } catch (Exception e) {
            page++;
            assert (true);
        }

        return page;
    }

    private void deleteSampleBlogs(int totalBlogs) throws DataNotFoundException, EntityException {
        int page = 0;
        int totalPages = totalBlogs / PaginationUtils.MAX_PAGE_SIZE;

        for (page = totalPages - 1; page >= 0; page--) {
            List<Blog> blogs = blogService.readAllBlogs(page);
            deleteBlogs(blogs);
        }
        int lastPageCount = totalBlogs % PaginationUtils.MAX_PAGE_SIZE;
        if (lastPageCount > 0) {
            List<Blog> blogs = blogService.readAllBlogs(0);
            deleteBlogs(blogs);
        }
    }

    private void deleteBlogs(List<Blog> blogs) {
        for (int i = 0; i < blogs.size(); i++) {
            deleteSampleBlog(blogs.get(i).getBlogId());
        }
    }

}
