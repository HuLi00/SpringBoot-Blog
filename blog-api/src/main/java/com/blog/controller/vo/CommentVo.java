package com.blog.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author: hu
 * @create: 2021-08-20 14:46
 */
@Data
public class CommentVo {
  //因为后端数据表中使用的主键是雪花算法（分布式数据库中比较好），
  // 但是返回在前端可能会损失精度，所以使用json转成String
//  @JsonSerialize(using = ToStringSerializer.class)
//  private Long id;
  private String id;
  private UserVo author;
  private String content;
  private List<CommentVo> childrens;
  private String createDate;
  private Integer level;
  private UserVo toUser;
}
