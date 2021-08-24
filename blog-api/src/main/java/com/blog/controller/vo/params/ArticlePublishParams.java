package com.blog.controller.vo.params;

import com.blog.controller.vo.CategoryVo;
import com.blog.controller.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author: hu
 * @create: 2021-08-20 20:20
 */
@Data
public class ArticlePublishParams {
  private String title;
  private Long id;
  private ArticleBodyParam body;
  private String summary;
  private List<TagVo> tags;
  private CategoryVo category;
}
