package com.blog.service;

import com.blog.controller.vo.LoginUserVo;
import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.UserLoginParams;
import com.blog.dao.pojo.SysUser;

public interface LoginService {
  //用户登录功能
  Result login(UserLoginParams userLoginParams);

  //检查Redis中否存在token值
  SysUser checkToken(String token);

  //登出/注销
  Result logout(String token);

  //用户注册接口
  Result register(UserLoginParams registerParam);
}
