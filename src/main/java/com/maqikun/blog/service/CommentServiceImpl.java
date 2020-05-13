package com.maqikun.blog.service;

import com.maqikun.blog.Dao.CommentRepository;
import com.maqikun.blog.pojo.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;



    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort orders = Sort.by(Sort.Direction.ASC,"createTime");
        List<Comment> comments=commentRepository.findByBlogIdAndParentCommentNull(blogId,orders);
        return eachComment(comments);
    }


    public Comment saveComment(Comment comment) {
        Long parentCommentId=comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
    /**
     * 循环每个顶级的评论节点
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentView=new ArrayList<>();
        for (Comment comment: comments) {
            Comment c=new Comment();
            BeanUtils.copyProperties(comment,c);
            commentView.add(c);
        }
        //合并评论的各层子代码到第一级子代码集合中
        combineChildren(commentView);
        return commentView;
    }
    /**
     *
     */
    private void combineChildren(List<Comment> comments){
        for(Comment comment:comments){
            List<Comment> replys1=comment.getReplyComments();
            for(Comment reply1:replys1){
                //循环迭代，找出子代存放tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时缓冲区
            tempReplys=new ArrayList<>();
        }
    }
    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys=new ArrayList<>();
    /**
     * 递归迭代
     */
    private void recursively(Comment comment){
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if(comment.getReplyComments().size()>0){
            List<Comment> replys=comment.getReplyComments();
            for(Comment reply:replys){
                tempReplys.add(reply);
                if(reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}
