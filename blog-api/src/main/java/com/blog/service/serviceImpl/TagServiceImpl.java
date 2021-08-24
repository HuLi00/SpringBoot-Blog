package com.blog.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.controller.vo.Result;
import com.blog.controller.vo.TagVo;
import com.blog.dao.mapper.TagMapper;
import com.blog.dao.pojo.Tag;
import com.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: hu
 * @create: 2021-08-17 22:40
 */
@Service
public class TagServiceImpl implements TagService {
  @Autowired
  private TagMapper tagMapper;

  @Override
  public List<TagVo> findTagsByArticleId(Long articleId) {
    //mybatis-plus 无法进行多表查询
    List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
    return copyList(tags);
  }

  public List<TagVo> copyList(List<Tag> tags){
    List<TagVo> tagVoList = new ArrayList<>();
    for (Tag tag : tags) {
      tagVoList.add(copy(tag));
    }
    return tagVoList;
  }

  public TagVo copy(Tag tag){
    TagVo tagVo = new TagVo();
    BeanUtils.copyProperties(tag, tagVo);
    tagVo.setId(String.valueOf(tag.getId()));
    return tagVo;
  }

  //查询热点标签
  @Override
  public Result queryHots(int limit) {
    List<Long> tagId = tagMapper.findHotsTagIds(limit);
    if(null == tagId){
      return Result.success(Collections.emptyList());
    }
    List<Tag> tags = tagMapper.findTagsByTagIds(tagId);
    return Result.success(tags);
  }

  @Override
  public Result getAllTags() {
    List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
    List<TagVo> tagVoList = copyList(tagList);
    return Result.success(tagVoList);
  }

  /*获取文章标签细节*/
  @Override
  public Result getAllTagsDetail() {
    List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
    return Result.success(copyList(tagList));
  }

  @Override
  public Result getTagById(Long id) {
    Tag tag = tagMapper.selectById(id);
    if(tag ==null){
      return Result.fail(10008,"无此文章标签");
    }
    return Result.success(copy(tag));
  }
}
