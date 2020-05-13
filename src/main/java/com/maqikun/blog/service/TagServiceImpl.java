package com.maqikun.blog.service;

import com.maqikun.blog.Dao.TagRepository;
import com.maqikun.blog.NotFoundException;
import com.maqikun.blog.pojo.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    public Tag saveTag(Tag Tag) {
        return tagRepository.save(Tag);
    }

    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }


    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag updateTag(Long id, Tag Tag) {
        Tag t=tagRepository.getOne(id);
        if(t==null){
            throw new NotFoundException("不存在给定类型");
        }
        BeanUtils.copyProperties(Tag,t);
        return tagRepository.save(t);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }
    public List<Long> convertToList(String ids){
        List<Long> list=new ArrayList<>();
        if(!"".equals(ids)&&ids!=null){
            String[] idArray = ids.split(",");
            for (int i=0;i<idArray.length;i++)
            list.add(new Long(idArray[i]));
        }
        return list;
    }


    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        PageRequest page = PageRequest.of(0, size, sort);
        return tagRepository.findTop(page);
    }
}
