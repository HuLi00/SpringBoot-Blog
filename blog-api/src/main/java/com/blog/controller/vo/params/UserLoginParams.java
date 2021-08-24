package com.blog.controller.vo.params;

import lombok.Data;

/**
 * @author: hu
 * @create: 2021-08-18 15:26
 */
@Data
public class UserLoginParams {
  private String account;
  private String password;
  private String nickname;
}
