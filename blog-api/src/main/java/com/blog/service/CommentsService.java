package com.blog.service;

import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.CommentParams;

public interface CommentsService {
//获取评论信息
  Result getArticleCommentByArticleId(Long articleId);
//  写评论
  Result writeComment(CommentParams commentParams);

}
