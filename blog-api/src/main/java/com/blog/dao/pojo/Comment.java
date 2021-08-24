package com.blog.dao.pojo;

import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-20 14:18
 */
@Data
public class Comment {
  private Long id;
  private String content;
  private Long createDate;
  private Long articleId;
  private Long authorId;
  private Long parentId;
  private Long toUid;
  private Long Level;
}
