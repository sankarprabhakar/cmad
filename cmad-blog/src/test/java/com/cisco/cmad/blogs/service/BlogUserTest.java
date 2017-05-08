package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.api.Users;

public class BlogUserTest {

    @Test
    public void createUser() {
        Users userService = UsersService.getInstance();
        User admin = new User();
        admin.setUserId("admin");
        admin.setEmailId("admin@tecblog.com");
        admin.setFirstName("admin");
        admin.setPassword("xyz");
        admin.setLastName("");

        try {
            userService.create(admin);
            User createdUser = userService.read("admin");
            assert (createdUser.getUserId() == "admin");
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
