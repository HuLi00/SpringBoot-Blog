package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hu
 * @create: 2021-08-18 10:48
 */
@RestController
@RequestMapping("tags")
public class TagController {
  @Autowired
  private TagService tagService;

  @GetMapping("hot")
  public Result hot(){
    //表示查询最热的3个标签
    int limit = 3;
    return tagService.queryHots(limit);
  }

  /*获取所有文章标签*/
  @GetMapping
  public Result tags(){
    return tagService.getAllTags();
  }

  /*获取所有文章标签细节*/
  @GetMapping("/detail")
  public Result tagsDetail(){
    return tagService.getAllTagsDetail();
  }

  /*通过标签Id获取标签*/
  @GetMapping("/detail/{id}")
  public Result getTagById(@PathVariable("id") Long id){
    return tagService.getTagById(id);
  }
}
