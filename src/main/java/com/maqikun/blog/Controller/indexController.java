package com.maqikun.blog.Controller;

import com.maqikun.blog.NotFoundException;
import com.maqikun.blog.pojo.Tag;
import com.maqikun.blog.service.BlogService;
import com.maqikun.blog.service.TagService;
import com.maqikun.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class indexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("/")
    public String index(@PageableDefault(size = 5,direction = Sort.Direction.DESC,sort = {"updateTime"}) Pageable pageable
    , Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }
    @PostMapping("/search")
    public String search(@PageableDefault(size = 5,direction = Sort.Direction.DESC,sort = {"updateTime"}) Pageable pageable,
                         Model model,@RequestParam String query){
        model.addAttribute("page",blogService.listBlog("%"+query+"%", pageable));
        model.addAttribute("query",query);
        return "search";
    }
    @GetMapping("/search")
    public String searchPage(@PageableDefault(size = 5,direction = Sort.Direction.DESC,sort = {"updateTime"}) Pageable pageable,
                         Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        return "search";
    }
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }
    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
