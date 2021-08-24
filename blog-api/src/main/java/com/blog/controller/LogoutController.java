package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hu
 * @create: 2021-08-18 19:25
 */
@RestController
@RequestMapping("logout")
public class LogoutController {
  @Autowired
  private LoginService loginService;

  @GetMapping
  public Result logout(@RequestHeader("Authorization") String token){
    return loginService.logout(token);
  }
}
