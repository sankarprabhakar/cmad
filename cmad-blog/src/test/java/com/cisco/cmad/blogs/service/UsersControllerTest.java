package com.cisco.cmad.blogs.service;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cisco.cmad.blogs.api.DataNotFoundException;
import com.cisco.cmad.blogs.api.DuplicateEntityException;
import com.cisco.cmad.blogs.api.EntityException;
import com.cisco.cmad.blogs.api.InvalidEntityException;
import com.cisco.cmad.blogs.api.User;
import com.cisco.cmad.blogs.api.Users;

public class UsersControllerTest {
    Users userService;

    User admin;

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
        System.out.println("@Before - runBeforeTestMethod");
    }

    // Should rename to @AfterTestMethod
    @After
    public void teardown() {
        deleteAdmin();
        System.out.println("@After - runAfterTestMethod");
    }

    void createAdmin() {
        User admin = new User();
        admin.setUserId("admin");
        admin.setEmailId("admin@tecblog.com");
        admin.setFirstName("admin");
        admin.setLastName("");
        admin.setPassword("xyz");
        userService.create(admin);
    }

    void createUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUserId("user" + i);
            user.setEmailId("user" + i + "@tecblog.com");
            user.setFirstName("firstName" + i);
            user.setLastName("lastName" + i);
            user.setPassword("xyz" + i);
            userService.create(user);
        }
    }

    private void validateUser(User user, String userId, String emailId, String firstName, String lastName) {
        assert (user.getUserId().compareTo(userId) == 0);
        assert (user.getEmailId().compareTo(emailId) == 0);
        assert (user.getFirstName().compareTo(firstName) == 0);
        assert (user.getLastName().compareTo(lastName) == 0);
    }

    private void deleteUsers(int count) {
        for (int i = 0; i < count; i++) {
            userService.delete("user" + i);
        }
    }

    private void verifyUsers(List<User> users, int count) {
        assert (users.size() == count);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUserId().compareTo("admin") != 0) {
                String id = user.getUserId();
                int suffix = Integer.parseInt(id.substring(id.length() - 1));
                String userId = "user" + suffix;
                String emailId = "user" + suffix + "@tecblog.com";
                String firstName = "firstName" + suffix;
                String lastName = "lastName" + suffix;
                validateUser(user, userId, emailId, firstName, lastName);
            }
        }
    }

    void deleteAdmin() {
        userService.delete("admin");
        admin = null;
    }

    @Test
    public void createUser() {
        try {
            createAdmin();
            User createdUser = userService.read("admin");
            validateUser(createdUser, "admin", "admin@tecblog.com", "admin", "");
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
            createAdmin();
            userService.authenticate("admin", "xyz");
        } catch (SecurityException se) {
            fail();
        } catch (EntityException ee) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void updateUser() {
        try {
            createAdmin();
            User oldUser = userService.read("admin");
            oldUser.setEmailId("testadmin@tecblog.com");
            oldUser.setFirstName("testadmin");
            userService.update(oldUser);

            User updatedUser = userService.read("admin");
            validateUser(updatedUser, "admin", "testadmin@tecblog.com", "testadmin", "");

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
    public void readAllUsers() {
        try {
            createAdmin();
            int count = 5;
            createUsers(count);
            List<User> users = userService.readAllUsers();
            verifyUsers(users, count + 1);
            deleteUsers(count);
        } catch (DataNotFoundException ibe) {
            fail();
        } catch (EntityException dbe) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void deleteUser() {
        try {
            createAdmin();
            userService.delete("admin");
            userService.read("admin");
        } catch (DataNotFoundException ibe) {
            createAdmin();
        } catch (EntityException dbe) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
