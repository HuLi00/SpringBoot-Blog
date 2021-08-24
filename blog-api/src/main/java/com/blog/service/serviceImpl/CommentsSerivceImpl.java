package com.blog.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.controller.vo.CommentVo;
import com.blog.controller.vo.Result;
import com.blog.controller.vo.UserVo;
import com.blog.controller.vo.params.CommentParams;
import com.blog.dao.mapper.ArticleMapper;
import com.blog.dao.mapper.CommentMapper;
import com.blog.dao.pojo.Article;
import com.blog.dao.pojo.Comment;
import com.blog.dao.pojo.SysUser;
import com.blog.service.CommentsService;
import com.blog.service.SysUserService;
import com.blog.utils.UserThreadLocalUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hu
 * @create: 2021-08-20 14:28
 */
@Service
public class CommentsSerivceImpl implements CommentsService {
  @Autowired
  private CommentMapper commentMapper;
  @Autowired
  private SysUserService sysUserService;
  @Autowired
  private ArticleMapper articleMapper;
  /**
  *
  *@Param: [articleId]
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *根据文章id查询评论列表
  */
  @Override
  public Result getArticleCommentByArticleId(Long articleId) {
    /*
    * 1、根据文章id 查询评论列表 从comment表中查询
    * 2、根据作者的id 查询作者信息
    * 3、判断 如果level = 1 要去查询它有没有子评论
    * 4、如果有子评论 根据评论id 进行查询（parent_id）
    * */
    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Comment::getArticleId, articleId);
    queryWrapper.eq(Comment::getLevel,1);
    List<Comment> commentList = commentMapper.selectList(queryWrapper);
    List<CommentVo> commentVoList = copyList(commentList);
    return Result.success(commentVoList);
  }

  public List<CommentVo> copyList(List<Comment> commentList){
    List<CommentVo> commentVoList = new ArrayList<>();
    for (Comment comment : commentList) {
      commentVoList.add(copy(comment));
    }
    return commentVoList;
  }

  public CommentVo copy(Comment comment){
    CommentVo commentVo = new CommentVo();
    BeanUtils.copyProperties(comment, commentVo);
    commentVo.setId(String.valueOf(comment.getId()));
    //通过作者Id查询作者信息
    Long authorId = comment.getAuthorId();
    UserVo userVo = sysUserService.findUserByAuthorId(authorId);
    commentVo.setAuthor(userVo);
    //子评论
    Long level = comment.getLevel();
    if(1 == level){
      Long id = comment.getId();
      List<CommentVo> commentVoList = findCommentsByParentId(id);
      commentVo.setChildrens(commentVoList);
    }
    //to User 给谁评论
    if(level > 1){
      Long toUid = comment.getToUid();
      UserVo toUserVo = sysUserService.findUserByAuthorId(toUid);
      commentVo.setToUser(toUserVo);
    }
    return commentVo;
  }

  public List<CommentVo> findCommentsByParentId(Long parentId){
    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Comment::getParentId, parentId);
    queryWrapper.eq(Comment::getLevel, 2);
    List<Comment> commentList = commentMapper.selectList(queryWrapper);
    List<CommentVo> commentVoList = copyList(commentList);
    return commentVoList;
  }

  /*评论功能*/
  @Override
  public Result writeComment(CommentParams commentParams) {
    SysUser user = UserThreadLocalUtils.get();
    Comment comment = new Comment();
    comment.setArticleId(commentParams.getArticleId());
    comment.setContent(commentParams.getContent());
    comment.setAuthorId(user.getId());
    comment.setCreateDate(System.currentTimeMillis());
    Long parent = commentParams.getParent();
    if(parent == null || 0 == parent){
      comment.setLevel(1L);
    }else{
      comment.setLevel(2L);
    }
    comment.setParentId(parent == null ? 0 : parent);
    Long toUserId = commentParams.getToUserId();
    comment.setToUid(toUserId == null ? 0 : toUserId);
    commentMapper.insert(comment);
    //更新评论数量
    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Comment::getArticleId, commentParams.getArticleId());
    Article article = new Article();
    article.setId(commentParams.getArticleId());
    article.setCommentCounts(commentMapper.selectCount(queryWrapper));
    articleMapper.updateById(article);
    return Result.success(null);
  }
}
