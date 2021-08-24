package com.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
  /*根据文章id去查询tag列表*/
  List<Tag> findTagsByArticleId(Long articleId);

  //查询前limit条热点信息
  List<Long> findHotsTagIds(int limit);

  //根据tagId来获取Tag对象
  List<Tag> findTagsByTagIds(List<Long> tagsId);
}
