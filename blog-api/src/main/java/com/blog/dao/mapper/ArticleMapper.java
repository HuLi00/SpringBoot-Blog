package com.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dao.dos.Archives;
import com.blog.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

  //文章分类接口
  List<Archives> listArchives();

  IPage<Article> listArticle(Page<Article> page,
                             Long categoryId,
                             Long tagId,
                             String year,
                             String month);
}
