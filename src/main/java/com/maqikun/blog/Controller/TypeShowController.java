package com.maqikun.blog.Controller;

import com.maqikun.blog.Vo.BlogQuery;
import com.maqikun.blog.pojo.Type;
import com.maqikun.blog.service.BlogService;
import com.maqikun.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TypeShowController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    @GetMapping({"/types/{id}"})
    public String types(@PageableDefault(size = 5,direction = Sort.Direction.DESC,sort = {"updateTime"}) Pageable pageable,
                        Model model, @PathVariable Long id){
        List<Type> types=typeService.listTypeTop(10000);
        if(id==-1){
            id=types.get(0).getId();
        }
        BlogQuery blogQuery=new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
