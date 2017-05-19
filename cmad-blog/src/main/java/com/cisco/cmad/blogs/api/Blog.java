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
@NamedQueries({ @NamedQuery(name = Blog.FIND_ALL, query = "SELECT b FROM Blog b ORDER BY b.lastUpdatedOn DESC"),
        @NamedQuery(name = Blog.COUNT_ALL, query = "SELECT COUNT(b) FROM Blog b"),
        @NamedQuery(name = Blog.FIND_BY_CATEGORY, query = "SELECT b FROM Blog b WHERE b.category = :category ORDER BY b.lastUpdatedOn DESC") })

public class Blog {
    public static final String FIND_ALL = "Blog.findAll";
    public static final String COUNT_ALL = "Blog.countAll";
    public static final String FIND_BY_CATEGORY = "Blog.findByCategory";
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

    public Blog setBlogId(long id) {
        this.blogId = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Blog setTitle(String title) {
        this.title = title;
        return this;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public Blog setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
        return this;
    }

    public String getBlogText() {
        return blogText;
    }

    public Blog setBlogText(String text) {
        this.blogText = text;
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public Blog setAuthor(User author) {
        this.author = author;
        // author.addBlog(this);
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Blog setCategory(String category) {
        this.category = category;
        return this;
    }

}
