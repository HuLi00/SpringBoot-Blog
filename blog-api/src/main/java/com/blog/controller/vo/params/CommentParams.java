package com.blog.controller.vo.params;

import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-20 18:53
 */
@Data
public class CommentParams {
  private Long articleId;
  private String content;
  private Long parent;
  private Long toUserId;
}
