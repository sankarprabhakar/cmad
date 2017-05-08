package com.cisco.cmad.blogs.api;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;

    @NotNull
    private String commentText;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpdatedOn;

    // (cascade = CascadeType.ALL, mappedBy = "blog", orphanRemoval =
    // true)
    // @ManyToOne(optional = false, fetch = FetchType.LAZY)
    // @ManyToOne // (optional = false)
    @ManyToOne(cascade = { CascadeType.REFRESH })
    // @JoinColumn(name = "userId")
    private User addedBy;

    // Specifies the Comment table does not contain an blog column, but
    // a blogId column with a foreign key. And creates a join to
    // fetch the blog
    // @ManyToOne(/* cascade = CascadeType.ALL, */fetch = FetchType.EAGER)
    // @JoinColumn(name = "blogId")
    // @ManyToOne // (optional = false)
    @ManyToOne(cascade = { CascadeType.REFRESH })
    // @ManyToOne
    private Blog blog;

    public Comment() {
        super();
    }

    public Comment(long commentId, String text, Date lastUpdatedOn) {
        super();
        this.commentId = commentId;
        this.commentText = text;
        this.lastUpdatedOn = lastUpdatedOn;
        // this.blog = blog;
        // this.addedBy = addedBy;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long id) {
        this.commentId = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String text) {
        this.commentText = text;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
        // blog.addComment(this);
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }
}
