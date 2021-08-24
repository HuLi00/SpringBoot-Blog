package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.UserLoginParams;
import com.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hu
 * @create: 2021-08-18 15:24
 */
@RestController
@RequestMapping("login")
public class LoginController {
  @Autowired
  private LoginService loginService;

  /**
  *
  *@Param: [userLoginParams]
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *用户登录
  */
  @PostMapping
  public Result login(@RequestBody UserLoginParams userLoginParams){
    return loginService.login(userLoginParams);
  }
}
