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

@Entity(name = "Comment")
@NamedQueries({
        @NamedQuery(name = Comment.COUNT_BLOG_COMMENTS, query = "SELECT COUNT(c) FROM Comment c WHERE c.blog.blogId = :blogId"),
        @NamedQuery(name = Comment.FIND_BLOG_COMMENTS, query = "SELECT c FROM Comment c WHERE c.blog.blogId = :blogId ORDER BY c.lastUpdatedOn DESC") })
public class Comment {
    public static final String FIND_BLOG_COMMENTS = "Blog.findBlogComments";
    public static final String COUNT_BLOG_COMMENTS = "Blog.countBlogComments";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;

    @NotNull
    private String commentText;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpdatedOn;

    @ManyToOne(cascade = { CascadeType.REFRESH })
    private User addedBy;

    @ManyToOne(cascade = { CascadeType.REFRESH })
    private Blog blog;

    public Comment() {
        super();
    }

    public long getCommentId() {
        return commentId;
    }

    public Comment setCommentId(long id) {
        this.commentId = id;
        return this;
    }

    public String getCommentText() {
        return commentText;
    }

    public Comment setCommentText(String text) {
        this.commentText = text;
        return this;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public Comment setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
        return this;
    }

    public Blog getBlog() {
        return blog;
    }

    public Comment setBlog(Blog blog) {
        this.blog = blog;
        return this;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public Comment setAddedBy(User addedBy) {
        this.addedBy = addedBy;
        return this;
    }
}
