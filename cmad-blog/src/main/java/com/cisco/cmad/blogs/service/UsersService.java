package com.cisco.cmad.blogs.service;

import java.util.List;

import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.api.Users;
import com.cisco.cmad.blogs.data.JPAUsersDAO;
import com.cisco.cmad.blogs.data.UsersDAO;

public class UsersService implements Users {
    private UsersDAO dao = new JPAUsersDAO();

    private static UsersService usersService = null;

    private UsersService() {
    }

    public static UsersService getInstance() {
        if (usersService == null) {
            usersService = new UsersService();
        }
        return usersService;
    }

    @Override
    public void create(User user) throws InvalidEntityException, DuplicateEntityException, EntityException {
        if (user == null)
            throw new InvalidEntityException();
        // if (dao.read(blog.getIsbn()) != null)
        // throw new DuplicateBlogException();
        dao.create(user);
    }

    @Override
    public User read(String userId) throws DataNotFoundException, EntityException {
        User user = dao.read(userId);
        if (user == null)
            throw new DataNotFoundException();
        return user;
    }

    @Override
    public List<User> readAllUsers() throws DataNotFoundException, EntityException {
        List<User> users = dao.readAllUsers();
        if (users == null)
            throw new DataNotFoundException();
        return users;
    }

    @Override
    public User authenticate(String userId, String password) throws SecurityException, EntityException {
        User user = dao.readByUserIdAndPassword(userId, password);

        if (user == null)
            throw new SecurityException("Invalid user/password");
        return user;
    }

    @Override
    public User update(User updatedUser) throws InvalidEntityException, DuplicateEntityException, EntityException {
        if (updatedUser == null)
            throw new InvalidEntityException();

        try {
            dao.update(updatedUser);
        } catch (Exception e) {
            throw new EntityException();
        }
        return updatedUser;
    }

    @Override
    public void delete(String userId) {
        read(userId);
        try {
            dao.delete(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityException();
        }
    }
}
