package com.blog.service;

import com.blog.controller.vo.CategoryVo;
import com.blog.controller.vo.Result;

public interface CategoryService {
  CategoryVo findCategoryByCategoryId(Long categoryId);

  //获取文章分类
  Result getAllCategorys();
  //获取更详细文章分类信息
  Result getAllCategorysDetail();
  //根据id获取文章分类信息
  Result findCategoryById(Long id);
}
