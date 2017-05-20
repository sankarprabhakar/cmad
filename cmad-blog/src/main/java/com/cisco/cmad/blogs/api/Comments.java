package com.cisco.cmad.blogs.api;

import java.util.List;

public interface Comments {
    public void create(Comment comment) throws InvalidEntityException, DuplicateEntityException, EntityException;

    public Comment read(long commentId) throws DataNotFoundException, EntityException;

    public List<Comment> readAllByBlogId(long blogId, int pageNum) throws DataNotFoundException, EntityException;

    public Comment update(Comment comment) throws DataNotFoundException, EntityException;

    public void delete(long id) throws EntityException;
}
