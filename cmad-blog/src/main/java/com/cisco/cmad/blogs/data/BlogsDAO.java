package com.cisco.cmad.blogs.data;

import java.util.List;

import com.cisco.cmad.blogs.api.Blog;

public interface BlogsDAO {
    public void create(Blog blog);

    public Blog read(long id);

    public List<Blog> readByCategory(String category, int pageNum);

    public List<Blog> readAllBlogs(int pageNum);

    public List<Blog> readByUserId(String userId, int pageNum);

    public void update(Blog blog);

    public void delete(long id);

}
