package com.maqikun.blog.Dao;

import com.maqikun.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>,JpaSpecificationExecutor<Blog> {
    @Query("select b from Blog b where b.recommended=true")
    List<Blog> findTop(Pageable pageable);
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findBlogByQuery(String query,Pageable pageable);
    @Modifying
    @Query("update Blog b set b.views=b.views+1 where b.id= :id")
    int updateViews(@Param("id") Long id);
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y')=?1")
    List<Blog> findBlogByYear(String year);
}
