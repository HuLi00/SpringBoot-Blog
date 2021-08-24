package com.blog.handler;

import com.blog.controller.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: hu
 * @create: 2021-08-18 12:43
 */
//@ControllerAdvice是一个Aop的注解
@ControllerAdvice
public class AllExceptionHandler {

  @ResponseBody
  @ExceptionHandler(Exception.class)
  public Result doExcepHandler(Exception e){
    e.printStackTrace();
    return Result.fail(-999,"系统异常");
  }
}
