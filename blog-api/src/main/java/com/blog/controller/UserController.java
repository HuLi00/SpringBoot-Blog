package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hu
 * @create: 2021-08-18 18:38
 */
@RestController
@RequestMapping("users")
public class UserController {
  @Autowired
  private SysUserService sysUserService;

  @GetMapping("/currentUser")
  public Result currentUser(@RequestHeader("Authorization") String token){
    return sysUserService.findUserByToken(token);
  }
}
