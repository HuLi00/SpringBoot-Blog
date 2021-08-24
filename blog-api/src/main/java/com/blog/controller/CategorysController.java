package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hu
 * @create: 2021-08-20 19:41
 */
@RestController
@RequestMapping("categorys")
public class CategorysController {
  @Autowired
  private CategoryService categoryService;

  /*获取文章分类*/
  @GetMapping
  public Result getCategorys(){
    return categoryService.getAllCategorys();
  }

  /*获取文章分类的详细信息*/
  @GetMapping("/detail")
  public Result getCategorysDetail(){
    return categoryService.getAllCategorysDetail();
  }

  /*获取分类文章列表*/
  @GetMapping("/detail/{id}")
  public Result getCategorysById(@PathVariable("id") Long id){
    return categoryService.findCategoryById(id);
  }
}
