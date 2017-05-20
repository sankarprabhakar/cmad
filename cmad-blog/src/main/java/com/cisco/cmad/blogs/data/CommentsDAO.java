package com.cisco.cmad.blogs.data;

import java.util.List;

import com.cisco.cmad.blogs.api.Comment;

public interface CommentsDAO {
    public void create(Comment comment);

    public Comment read(long commentId);

    public List<Comment> readAllByBlogId(long blogId, int pageNum);

    public void update(Comment comment);

    public void delete(long id);
}
