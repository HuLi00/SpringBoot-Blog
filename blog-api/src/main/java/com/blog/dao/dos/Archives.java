package com.blog.dao.dos;

import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-18 14:47
 * 这个dos包中的对象是数据库中没有的
 */
@Data
public class Archives {
  private Integer year;
  private Integer month;
  private Long count;
}
