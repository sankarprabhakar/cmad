package com.cisco.cmad.blogs.api;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity(name = "User")
@NamedQueries({ @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u ORDER BY u.firstName DESC"),
        @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password"),
        @NamedQuery(name = User.COUNT_ALL, query = "SELECT COUNT(u) FROM User u"),
        @NamedQuery(name = User.DELETE_BLOGS_BY_USER_ID, query = "DELETE FROM Blog b WHERE b.author.userId = :userId"),
        @NamedQuery(name = User.DELETE_COMMENTS_BY_USER_ID, query = "DELETE FROM Comment c WHERE c.addedBy.userId = :userId") })
public class User {
    public static final String FIND_ALL = "User.findAll";
    public static final String COUNT_ALL = "User.countAll";
    public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";
    public static final String DELETE_BLOGS_BY_USER_ID = "User.deleteBlogsByUserId";
    public static final String DELETE_COMMENTS_BY_USER_ID = "User.deleteCommentsByUserId";

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

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public User setId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFullName() {
        return (lastName.isEmpty()) ? firstName : firstName + " " + lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public User setEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    // public List<Blog> getBlogs() {
    // return blogs;
    // }
    //
    // public User setBlogs(List<Blog> blogs) {
    // this.blogs = blogs;
    // return this;
    // }
    //
    // public void addBlog(Blog blog) {
    // blogs.add(blog);
    // }

    // public List<Comment> getComments() {
    // return comments;
    // }
    //
    // public User setComments(List<Comment> comments) {
    // this.comments = comments;
    // return this;
    // }
}