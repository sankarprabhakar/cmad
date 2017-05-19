package com.cisco.cmad.blogs.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.cisco.cmad.blogs.api.Blog;
import com.cisco.cmad.blogs.common.config.AppConfig;

public class JPABlogsDAO implements BlogsDAO {
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory(AppConfig.PERSISTENCE_UNIT_NAME);

    @Override
    public void create(Blog blog) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(blog);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    // Search blogs by category
    // @pageNum - 0 based page index
    public List<Blog> readByCategory(String category, int pageNum) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Blog> tquery = em.createNamedQuery(Blog.FIND_BY_CATEGORY, Blog.class);
        tquery.setParameter("category", category);
        setPageParams(tquery, pageNum);
        List<Blog> blogs = tquery.getResultList();
        em.getTransaction().commit();
        em.close();
        return blogs;
    }

    @Override
    // Read all blogs
    // @pageNum - 0 based page index
    public List<Blog> readAllBlogs(int pageNum) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Blog> tquery = em.createNamedQuery(Blog.FIND_ALL, Blog.class);
        setPageParams(tquery, pageNum);
        List<Blog> blogs = tquery.getResultList();
        em.getTransaction().commit();
        em.close();
        return blogs;
    }

    // Find a blog by id
    @Override
    public Blog read(long blogId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Blog blog = em.find(Blog.class, blogId);
        em.getTransaction().commit();
        em.close();
        return blog;
    }

    // Update a blog
    @Override
    public void update(Blog updatedBlog) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Blog blog = em.find(Blog.class, updatedBlog.getBlogId());
        blog.setBlogText(updatedBlog.getBlogText());
        blog.setTitle(updatedBlog.getTitle());
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(long blogId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Blog blog = em.find(Blog.class, blogId);
        em.remove(blog);
        deleteCommentsByBlogId(blog.getBlogId(), em);
        em.getTransaction().commit();
        em.close();
    }

    private void deleteCommentsByBlogId(long blogId, EntityManager em) {
        Query query = em.createNamedQuery(Blog.DELETE_BLOG_COMMENTS);
        query.setParameter("blogId", blogId);
        query.executeUpdate();
    }

    @Override
    public List<Blog> readByUserId(String userId, int pageNum) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Blog> tquery = em.createNamedQuery(Blog.FIND_USER_BLOGS, Blog.class);
        setPageParams(tquery, pageNum);
        List<Blog> blogs = tquery.setParameter("userId", userId).getResultList();
        em.getTransaction().commit();
        em.close();
        return blogs;
    }

    private void setPageParams(TypedQuery<Blog> tquery, int pageNum) {
        tquery.setMaxResults(AppConfig.MAX_PAGE_SIZE);
        int index = pageNum * AppConfig.MAX_PAGE_SIZE;
        tquery.setFirstResult(index);
    }
}
