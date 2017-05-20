package com.cisco.cmad.blogs.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.cisco.cmad.blogs.api.Comment;
import com.cisco.cmad.blogs.common.config.AppConfig;

public class JPACommentsDAO implements CommentsDAO {

    private EntityManagerFactory factory = Persistence.createEntityManagerFactory(AppConfig.PERSISTENCE_UNIT_NAME);

    @Override
    public void create(Comment comment) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(comment);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Comment read(long commentId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Comment comment = em.find(Comment.class, commentId);
        em.getTransaction().commit();
        em.close();
        return comment;
    }

    @Override
    public void update(Comment updatedComment) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Comment comment = em.find(Comment.class, updatedComment.getCommentId());
        comment.setCommentText(updatedComment.getCommentText());
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(long commentId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Comment comment = em.find(Comment.class, commentId);
        em.remove(comment);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Comment> readAllByBlogId(long blogId, int pageNum) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Comment> tquery = em.createNamedQuery(Comment.FIND_BLOG_COMMENTS, Comment.class);
        setPageParams(tquery, pageNum);
        List<Comment> comments = tquery.setParameter("blogId", blogId).getResultList();
        em.getTransaction().commit();
        em.close();
        return comments;
    }

    private void setPageParams(TypedQuery<Comment> tquery, int pageNum) {
        tquery.setMaxResults(AppConfig.MAX_COMMENTS_PAGE_SIZE);
        int index = pageNum * AppConfig.MAX_COMMENTS_PAGE_SIZE;
        tquery.setFirstResult(index);
    }

}
