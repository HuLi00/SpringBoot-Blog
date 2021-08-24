package com.blog.dao.pojo;

import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-17 16:20
 */
@Data
public class Article {
  public static final int Article_TOP = 1;

  public static final int Article_Common = 0;

  private Long id;

  private Integer commentCounts;

  private Long createDate;

  private String summary;

  private String title;

  private Integer viewCounts;

  private Integer weight;

  private Long authorId;

  private Long bodyId;

  private Long categoryId;
}
