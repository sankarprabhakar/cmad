package com.cisco.cmad.blogs.service;

import java.util.ArrayList;
import java.util.List;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.api.Blogs;
import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.data.BlogsDAO;
import com.cisco.cmad.blogs.data.JPABlogsDAO;

public class BlogsService implements Blogs {
    private BlogsDAO dao = new JPABlogsDAO();
    private static BlogsService blogsService = null;

    private BlogsService() {
    }

    public static BlogsService getInstance() {
        if (blogsService == null) {
            blogsService = new BlogsService();
        }
        return blogsService;
    }

    @Override
    public void create(Blog blog) throws InvalidEntityException, DuplicateEntityException, EntityException {
        if (blog == null)
            throw new InvalidEntityException();
        if (dao.read(blog.getBlogId()) != null)
            throw new DuplicateEntityException();
        dao.create(blog);
    }

    @Override
    public List<Blog> readByCategory(String category, int pageNum) throws DataNotFoundException, EntityException {
        List<Blog> blogs = new ArrayList<Blog>();
        try {
            blogs = dao.readByCategory(category, pageNum);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityException();
        }

        if (blogs == null || blogs.isEmpty())
            throw new DataNotFoundException();
        return blogs;
    }

    @Override
    public List<Blog> readAllBlogs(int pageNum) throws DataNotFoundException, EntityException {
        List<Blog> blogs = new ArrayList<Blog>();
        try {
            blogs = dao.readAllBlogs(pageNum);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityException();
        }

        if (blogs == null || blogs.isEmpty())
            throw new DataNotFoundException();
        return blogs;
    }

    @Override
    public Blog read(long blogId) throws DataNotFoundException, EntityException {
        Blog blog = null;
        try {
            blog = dao.read(blogId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityException();
        }

        if (blog == null)
            throw new DataNotFoundException();

        return blog;
    }

    @Override
    public Blog update(Blog updatedBlog) throws InvalidEntityException, DataNotFoundException, EntityException {
        if (updatedBlog == null)
            throw new InvalidEntityException();

        try {
            dao.update(updatedBlog);
        } catch (Exception e) {
            throw new EntityException();
        }
        return updatedBlog;
    }

    @Override
    public void delete(long blogId) throws DataNotFoundException, EntityException {
        read(blogId);
        try {
            dao.delete(blogId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityException();
        }
    }

    @Override
    public List<Blog> readByUserId(String userId, int pageNum) throws DataNotFoundException, EntityException {
        List<Blog> blogs = new ArrayList<Blog>();
        try {
            blogs = dao.readByUserId(userId, pageNum);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityException();
        }

        if (blogs == null || blogs.isEmpty())
            throw new DataNotFoundException();
        return blogs;
    }

}
