package com.blog.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.controller.vo.CategoryVo;
import com.blog.controller.vo.ErrorCode;
import com.blog.controller.vo.Result;
import com.blog.dao.mapper.CategoryMapper;
import com.blog.dao.pojo.Category;
import com.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hu
 * @create: 2021-08-19 20:51
 */
@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  private CategoryMapper categoryMapper;
  @Override
  public CategoryVo findCategoryByCategoryId(Long categoryId) {
    Category category = categoryMapper.selectById(categoryId);
    CategoryVo categoryVo = new CategoryVo();
    BeanUtils.copyProperties(category, categoryVo);
    categoryVo.setId(String.valueOf(category.getId()));
    return categoryVo;
  }

  @Override
  public Result getAllCategorys() {
    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//    queryWrapper.select(Category::getId, Category::getCategoryName);
    List<Category> categoryList = categoryMapper.selectList(queryWrapper);
    List<CategoryVo> categoryVoList = copyList(categoryList);
    return Result.success(categoryList);
  }

  public List<CategoryVo> copyList(List<Category> categoryList){
    List<CategoryVo> categoryVoList = new ArrayList<>();
    for (Category category : categoryList) {
      categoryVoList.add(copy(category));
    }
    return  categoryVoList;
  }

  public CategoryVo copy(Category category){
    CategoryVo categoryVo = new CategoryVo();
    BeanUtils.copyProperties(category, categoryVo);
    categoryVo.setId(String.valueOf(category.getId()));
    return categoryVo;
  }

  @Override
  public Result getAllCategorysDetail() {
    List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<>());
    return Result.success(copyList(categoryList));
  }

  @Override
  public Result findCategoryById(Long id) {
    Category category = categoryMapper.selectById(id);
    if(category == null){
      return Result.fail(10008,"无此分类id");
    }
    return Result.success(copy(category));
  }
}
