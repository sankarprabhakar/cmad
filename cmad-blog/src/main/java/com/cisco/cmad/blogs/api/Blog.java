package com.cisco.cmad.blogs.api;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "Blog")
@NamedQueries({ @NamedQuery(name = Blog.FIND_ALL, query = "SELECT u FROM Blog u ORDER BY u.lastUpdatedOn DESC"),
        @NamedQuery(name = Blog.COUNT_ALL, query = "SELECT COUNT(u) FROM Blog u") })

public class Blog {
    public static final String FIND_ALL = "Blog.findAll";
    public static final String COUNT_ALL = "Blog.countAll";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long blogId;

    @NotNull
    private String title;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpdatedOn;

    private String blogText;

    private String category;

    // @NotNull
    // Specifies the Blog table does not contain an author column, but
    // an userId column with a foreign key. And creates a join to
    // fetch the userId
    // @ManyToOne(cascade = CascadeType.MERGE REFRESH, fetch = FetchType.LAZY)
    // @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @ManyToOne(cascade = { CascadeType.REFRESH })
    // @JoinColumn(name = "userId")
    private User author;

    // should contain one to many comments
    // The 'mappedBy = "blog"' attribute specifies that
    // the 'private Blog blog;' field in Comment owns the
    // relationship (i.e. contains the foreign key for the query to
    // find all comments for the blog.)
    // @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // private List<Comment> comments;
    // @JsonManagedReference
    // @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER,
    // mappedBy = "blog", orphanRemoval = true)
    // private List<Comment> comments = new ArrayList<>();
    // should contain one to many media files
    // todo:

    // todo: add support for storing blog html

    public Blog() {
    }

    public Blog(long id, String title, User author, Date lastUpdatedOn, String blogText) {
        super();
        this.title = title;
        this.author = author;
        this.lastUpdatedOn = lastUpdatedOn;
        this.blogText = blogText;
        this.blogId = id;
    }

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long id) {
        this.blogId = id;
    }

    // public List<Comment> getComments() {
    // return comments;
    // }
    //
    // public void setComments(List<Comment> comments) {
    // this.comments = comments;
    // }
    //
    // public void addComment(Comment comment) {
    // if (comments != null) {
    // comments.add(comment);
    // }
    // }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String text) {
        this.blogText = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
        // author.addBlog(this);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
