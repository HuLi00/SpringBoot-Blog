package com.blog.service;

import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.ArticlePublishParams;
import com.blog.controller.vo.params.PageParams;

public interface ArticleService {
  //分页查询文章列表
  Result listArticle(PageParams pageParams);

  //根据浏览量获取最热文章
  Result getHotArticles(int limit);

  //根据时间获取最新文章
  Result getNewArticle(int limit);

  //文章归档
  Result listArchives();

  Result findArticleById(Long id);

  //文章发布
  Result publishArticle(ArticlePublishParams articlePublishParams);
}
