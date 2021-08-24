package com.blog.dao.pojo;

import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-19 19:39
 */
@Data
public class ArticleBody {
  private Long id;
  private String content;
  private String contentHtml;
  private Long articleId;
}
