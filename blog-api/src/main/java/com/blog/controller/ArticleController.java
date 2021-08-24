package com.blog.controller;

import com.blog.common.aop.Cache;
import com.blog.common.aop.ClearCache;
import com.blog.common.aop.LogAnnotation;
import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.ArticlePublishParams;
import com.blog.controller.vo.params.PageParams;
import com.blog.dao.pojo.Article;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hu
 * @create: 2021-08-17 16:03
 */

@RestController
@RequestMapping("articles")
public class ArticleController {
  @Autowired
  private ArticleService articleService;
//  @RequestMapping(method = RequestMethod.POST)
  @PostMapping
  /**
  *
  *@Param: [pageParams]
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *首页文章
  */
  @Cache(expire = 5*60*1000, name = "articles")
  public Result articles(@RequestBody PageParams pageParams){
    return articleService.listArticle(pageParams);
  }

  /**
  *
  *@Param: []
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *首页最热文章
  */
  @Cache(expire = 5*60*1000, name = "hot_article")
  @PostMapping("/hot")
  public Result hotArticles(){
    //根据浏览量返回前3条浏览量最高的标签
    int limit = 5;
    return articleService.getHotArticles(limit);
  }

  /**
  *
  *@Param: []
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *获取首页最新文章
  */
  @Cache(expire = 5*60*1000, name = "new_article")
  @PostMapping("/new")
  public Result newArticle(){
    int limit = 5;
    return articleService.getNewArticle(limit);
  }

  /**
  *
  *@Param: []
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *首页中文章归档
  */
  @PostMapping("/listArchives")
  public Result archives(){
    return articleService.listArchives();
  }

  /**
  *
  *@Param: []
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *文章详情
  */
  @PostMapping("/view/{id}")
  public Result articleDetail(@PathVariable Long id){
    return articleService.findArticleById(id);
  }


  @PostMapping("/publish")
  @LogAnnotation(module = "文章模块", operator = "发布文章接口")
  @ClearCache
  public Result publishArticle(@RequestBody ArticlePublishParams articlePublishParams){
    return articleService.publishArticle(articlePublishParams);
  }
}
