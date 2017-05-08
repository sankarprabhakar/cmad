package com.cisco.cmad.blogs.api;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity(name = "User")
@NamedQueries({ @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u ORDER BY u.firstName DESC"),
        @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password"),
        @NamedQuery(name = User.COUNT_ALL, query = "SELECT COUNT(u) FROM User u") })
public class User {
    public static final String FIND_ALL = "User.findAll";
    public static final String COUNT_ALL = "User.countAll";
    public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";

    @Id
    private String userId;

    @NotNull
    private String password;
    @NotNull
    private String firstName;
    private String lastName;
    @NotNull
    private String emailId;

    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)mappedBy =
    // "author",
    // @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // // @JoinColumn(name = "blogId")
    // private List<Blog> blogs;
    // @JsonBackReference
    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", orphanRemoval
    // = true)
    // private List<Blog> blogs = new ArrayList<>();

    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "addedBy", orphanRemoval
    // = true)
    // private List<Comment> comments = new ArrayList<>();

    public User() {
        super();
    }

    public User(String userId, String password, String firstName, String lastName, String emailId) {
        super();
        this.userId = userId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return (lastName.isEmpty()) ? firstName : firstName + " " + lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    // public List<Blog> getBlogs() {
    // return blogs;
    // }
    //
    // public void setBlogs(List<Blog> blogs) {
    // this.blogs = blogs;
    // }
    //
    // public void addBlog(Blog blog) {
    // blogs.add(blog);
    // }

    // public List<Comment> getComments() {
    // return comments;
    // }
    //
    // public void setComments(List<Comment> comments) {
    // this.comments = comments;
    // }
}