package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.api.Users;

public class BlogUserTest {

	// assert (true);
	Users userService = UsersService.getInstance();
	User admin = new User();

	@Test
    public void createUser() {
        admin.setUserId("admin");
        admin.setEmailId("admin@tecblog.com");
        admin.setFirstName("admin");
        admin.setPassword("xyz");
        admin.setLastName("");

        try {
            userService.create(admin);
            User createdUser = userService.read("admin");
            assert (createdUser.getUserId() == "admin");
            assert (createdUser.getEmailId() == "admin@tecblog.com");
            assert (createdUser.getFirstName() == "admin");
            assert (createdUser.getLastName() == "");
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
    public void authenticateUser() {
        try {
            userService.authenticate("admin", "xyz");
        } catch (SecurityException se) {
            fail();
        } catch (EntityException  ee) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

	@Test
    public void updateUser() {

        admin.setEmailId("testadmin@tecblog.com");
        admin.setFirstName("testadmin");

        try {
            userService.update(admin);
            User createdUser = userService.read("admin");
            
            assert (createdUser.getUserId() == "admin");
            assert (createdUser.getEmailId() == "testadmin@tecblog.com");
            assert (createdUser.getFirstName() == "testadmin");
            
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
    public void deleteUser() {

        try {
            userService.delete("admin");
            User createdUser = userService.read("admin");
            
            assert (createdUser.getUserId() == "admin");
            assert (createdUser.getEmailId() == "testadmin@tecblog.com");
            assert (createdUser.getFirstName() == "testadmin");
            
        } catch (DataNotFoundException ibe) {
            fail();
        } catch (EntityException dbe) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
