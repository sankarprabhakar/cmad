package com.cisco.cmad.blogs.data;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.jwt.utils.PasswordUtils;

public class JPAUsersDAO implements UsersDAO {
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.cisco.blogs");

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void create(User user) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public User read(String userId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, userId);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    @Override
    public User readByUserIdAndPassword(String userId, String password) {
        logger.info("readByUserIdAndPassword userId: " + userId + "  password: " + password);
        EntityManager em = factory.createEntityManager();
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
        query.setParameter("userId", userId);
        logger.info("encrypted password: " + PasswordUtils.encryptPassword(password));
        query.setParameter("password", PasswordUtils.encryptPassword(password));
        User user = query.getSingleResult();
        return user;
    }

    @Override
    public List<User> readAllUsers() {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<User> tquery = em.createNamedQuery(User.FIND_ALL, User.class);
        List<User> users = tquery.getResultList();
        em.getTransaction().commit();
        em.close();
        return users;
    }

    @Override
    public void update(User updatedUser) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, updatedUser.getUserId());
        user.setEmailId(updatedUser.getEmailId());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(String userId) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, userId);
        em.remove(user);
        deleteBlogsByUserId(userId, em);
        deleteCommentsByUserId(userId, em);
        em.getTransaction().commit();
        em.close();
    }

    private void deleteBlogsByUserId(String userId, EntityManager em) {
        String queryStr = "DELETE FROM Blog b WHERE b.author.userId = :userId";
        Query query = em.createQuery(queryStr);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    private void deleteCommentsByUserId(String userId, EntityManager em) {
        String queryStr = "DELETE FROM Comment c WHERE c.addedBy.userId = :userId";
        Query query = em.createQuery(queryStr);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

}
