package com.maqikun.blog.Controller.admin;

import com.maqikun.blog.pojo.Tag;
import com.maqikun.blog.pojo.Type;
import com.maqikun.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;
    /*
    回到index页面
     */
    @GetMapping("/tagToIndex")
    public String index(){
        return "admin/index";
    }
    /*
    回到分类界面
     */
    @GetMapping("/tagToTypes")
    public String types() {
        return "redirect:/admin/types";
    }
    /*
    页面分页
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }
    /*
    新增标签
     */
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tag-input";
    }
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        if(tagService.getTagByName(tag.getName())!=null){
            result.rejectValue("name","nameError","该标签已经存在！");
        }
        if(result.hasErrors()){
            return "admin/tag-input";
        }
        Tag t = tagService.saveTag(tag);
        if(t==null&&t.getName().equals(" ")){
            attributes.addFlashAttribute("message","新增失败！");
        }else{
            attributes.addFlashAttribute("message","新增成功！");
        }
        return "redirect:/admin/tags";
    }
    /*
    修改标签
     */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tag-input";
    }
    /*
    修改
     */
    @PostMapping("/tags/{id}")  //注意BindingResult一定要跟被校验对象紧挨着
    public String editpost(@Valid Tag Tag, BindingResult result,@PathVariable Long id,RedirectAttributes attributes){
        if(tagService.getTagByName(Tag.getName())!=null){
            result.rejectValue("name","nameError","该标签已经存在！");
        }
        if(result.hasErrors()){
            return "admin/tag-input";
        }
        Tag t = tagService.updateTag(id,Tag);
        if(t==null&&t.getName().equals(" ")){
            attributes.addFlashAttribute("message","更新失败！");
        }else{
            attributes.addFlashAttribute("message","更新成功！");
        }
        return "redirect:/admin/tags";
    }
    /*
    删除分类
     */
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        //带着message属性去下一个页面
        attributes.addFlashAttribute("message","删除成功!");
        return "redirect:/admin/tags";
    }
}
