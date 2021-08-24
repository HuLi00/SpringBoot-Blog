package com.blog.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-20 14:48
 */
@Data
public class UserVo {
  private String nickname;
  private String avatar;
//  private Long id;
  private String id;
}
