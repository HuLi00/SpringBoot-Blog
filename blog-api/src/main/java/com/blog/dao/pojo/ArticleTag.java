package com.blog.dao.pojo;

import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-20 21:11
 */
@Data
public class ArticleTag {
  private Long id;
  private Long articleId;
  private Long tagId;
}
