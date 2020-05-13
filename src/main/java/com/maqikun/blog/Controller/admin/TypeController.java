package com.maqikun.blog.Controller.admin;

import com.maqikun.blog.pojo.Type;
import com.maqikun.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;
    /*
    回到index页面
     */
    @GetMapping("/TypesToIndex")
    public String index(){
        return "admin/index";
    }
    /*
    到标签界面
     */
    @GetMapping("/TypesToTags")
    public String tags(){
        return "redirect:/admin/tags";
    }

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }
    @PostMapping("/types")//BindingResult要紧跟@Valid表示校验的结果
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        if(typeService.getTypeByName(type.getName())!=null){
            result.rejectValue("name","nameError","该分类已经存在！");
        }
        if(result.hasErrors()){
            return "admin/type-input";
        }
        Type t = typeService.saveType(type);
        if(t==null&&t.getName().equals(" ")){
            attributes.addFlashAttribute("message","新增失败！");
        }else{
            attributes.addFlashAttribute("message","新增成功！");
        }
        return "redirect:/admin/types";
    }
    /*
    修改分类
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/type-input";
    }
    /*
    修改
     */
    @PostMapping("/types/{id}")  //注意BindingResult一定要跟被校验对象紧挨着
    public String editpost(@Valid Type type, BindingResult result,@PathVariable Long id,RedirectAttributes attributes){
        if(typeService.getTypeByName(type.getName())!=null){
            result.rejectValue("name","nameError","该分类已经存在！");
        }
        if(result.hasErrors()){
            return "admin/type-input";
        }
        Type t = typeService.updateType(id,type);
        if(t==null&&t.getName().equals(" ")){
            attributes.addFlashAttribute("message","更新失败！");
        }else{
            attributes.addFlashAttribute("message","更新成功！");
        }
        return "redirect:/admin/types";
    }
    /*
    删除分类
     */
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        //带着message属性去下一个页面
        attributes.addFlashAttribute("message","删除成功!");
        return "redirect:/admin/types";
    }
}
