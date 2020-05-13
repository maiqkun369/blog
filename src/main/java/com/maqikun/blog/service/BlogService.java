package com.maqikun.blog.service;

import com.maqikun.blog.Vo.BlogQuery;
import com.maqikun.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    Blog saveBlog(Blog blog);
    Blog updateBlog(Long id,Blog blog);
    void deleteBlog(Long id);
    Page<Blog> listBlog(Pageable pageable);
    List<Blog> listRecommendBlogTop(Integer size);
    Page<Blog> listBlog(String query,Pageable pageable);
    Blog getAndConvert(Long id);
    Page<Blog> listBlog(Long TagId,Pageable pageable);
    Map<String,List<Blog>> archiveBlog();
    Long countBlog();
}
