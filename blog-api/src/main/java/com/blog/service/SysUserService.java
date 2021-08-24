package com.blog.service;

import com.blog.controller.vo.Result;
import com.blog.controller.vo.UserVo;
import com.blog.dao.pojo.SysUser;
import com.sun.org.apache.xpath.internal.operations.Bool;

public interface SysUserService {
  //通过作者Id找到用户
  SysUser findUserNameByAuthorId(long userId);

  //通过用户名密码查询用户
  SysUser findUser(String userAccount, String userPassword);

  //根据token获取用户信息
  Result findUserByToken(String token);

  //根据account获取用户
  SysUser findUserByAccount(String Account);

  //向数据库中注册用户
  void registerUser(SysUser user);

  //
  UserVo findUserByAuthorId(Long authorId);
}
