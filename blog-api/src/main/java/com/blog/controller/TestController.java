package com.blog.controller;

import com.blog.controller.vo.Result;
import com.blog.dao.pojo.SysUser;
import com.blog.utils.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hu
 * @create: 2021-08-18 22:41
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {
  @PostMapping
  public Result test(){
    //进行接口的拦截后，这里可以使用ThreadLocal中保存的sysUser对象
    SysUser sysUser = UserThreadLocalUtils.get();
    log.info("user info:{}",sysUser);
//    System.out.println(sysUser);
    return Result.success(null);
  }
}
