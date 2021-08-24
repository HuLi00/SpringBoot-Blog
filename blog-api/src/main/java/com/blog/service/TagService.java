package com.blog.service;

import com.blog.controller.vo.Result;
import com.blog.controller.vo.TagVo;

import java.util.List;

public interface TagService {
  //根据文章id来获取标签
  List<TagVo> findTagsByArticleId(Long articleId);
  //查询前limit条热点标签
  Result queryHots(int limit);
  //获取所有文章标签
  Result getAllTags();

  Result getAllTagsDetail();

  Result getTagById(Long id);
}
