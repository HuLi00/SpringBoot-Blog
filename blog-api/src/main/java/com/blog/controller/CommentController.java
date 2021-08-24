package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.CommentParams;
import com.blog.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hu
 * @create: 2021-08-20 14:22
 */
@RestController
@RequestMapping("comments")
public class CommentController {
  @Autowired
  private CommentsService commentsService;
  /*根据文章的id获取文章的评论*/
  @GetMapping("article/{id}")
  public Result articleComment(@PathVariable("id") Long articleId){
    return commentsService.getArticleCommentByArticleId(articleId);
  }

  @PostMapping("create/change")
  public Result commentChange(@RequestBody CommentParams commentParams){
    return commentsService.writeComment(commentParams);
  }
}
