package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.UserLoginParams;
import com.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hu
 * @create: 2021-08-18 19:43
 */
@RestController
@RequestMapping("register")
public class RegisterController {
  @Autowired
  private LoginService loginService;

  @PostMapping
  public Result register(@RequestBody UserLoginParams registerParam){
    return loginService.register(registerParam);
  }
}
