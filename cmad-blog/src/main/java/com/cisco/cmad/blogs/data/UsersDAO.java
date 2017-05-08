package com.cisco.cmad.blogs.data;

import java.util.List;

import com.cisco.cmad.blogs.api.User;

public interface UsersDAO {
    public void create(User user);

    public User read(String userId);

    public User readByUserIdAndPassword(String userId, String password);

    public List<User> readAllUsers();

    public void update(User user);

    public void delete(String userId);
}
