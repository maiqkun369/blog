package com.maqikun.blog.Controller;

import com.maqikun.blog.Vo.BlogQuery;
import com.maqikun.blog.pojo.Tag;
import com.maqikun.blog.service.BlogService;
import com.maqikun.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {
    @Autowired
    private TagService TagService;
    @Autowired
    private BlogService blogService;
    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 5,direction = Sort.Direction.DESC,sort = {"updateTime"}) Pageable pageable,
                        Model model, @PathVariable Long id){
        List<Tag> tags=TagService.listTagTop(10000);
        if(id==-1){
            id=tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);
        return "tags";
    }
}
