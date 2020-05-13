package com.maqikun.blog.service;

import com.maqikun.blog.Dao.BlogRepository;
import com.maqikun.blog.NotFoundException;
import com.maqikun.blog.Vo.BlogQuery;
import com.maqikun.blog.pojo.Blog;
import com.maqikun.blog.pojo.Type;
import com.maqikun.blog.utils.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maqikun.blog.utils.MarkDownUtils;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.jar.JarEntry;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public Blog getBlog(Long id) {

        return blogRepository.getOne(id);
    }

    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkDownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        //处理动态查询条件 类似Hibernate 的离线查询
        return blogRepository.findAll(new Specification<Blog>() {
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    //封装查询条件<String>定制类型
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommended()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommended"), blog.isRecommended()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }


    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdataTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdataTime(new Date());
        }
        return blogRepository.save(blog);
    }


    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        //b是被COPY的对象blog->b
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdataTime(new Date());
        return blogRepository.save(b);
    }


    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }


    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }


    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        PageRequest page = PageRequest.of(0, size, sort);
        return blogRepository.findTop(page);
    }


    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findBlogByQuery(query, pageable);
    }


    public Page<Blog> listBlog(Long TagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), TagId);
            }
        }, pageable);
    }


    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogRepository.findGroupYear();
        Map<String, List<Blog>> map=new HashMap<>();
        for(String year:years){
            map.put(year,blogRepository.findBlogByYear(year));
        }
        return map;
    }


    public Long countBlog() {
        return blogRepository.count();
    }
}