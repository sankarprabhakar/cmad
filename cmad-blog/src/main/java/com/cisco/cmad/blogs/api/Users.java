package com.cisco.cmad.blogs.api;

import java.util.List;

public interface Users {
    public void create(User user) throws InvalidEntityException, DuplicateEntityException, EntityException;

    public User read(String userId) throws DataNotFoundException, EntityException;

    public List<User> readAllUsers() throws DataNotFoundException, EntityException;

    // authenticate = find
    public User authenticate(String userId, String password) throws SecurityException, EntityException;

    public User update(User user) throws InvalidEntityException, DuplicateEntityException, EntityException;

    public void delete(String userId);
}
