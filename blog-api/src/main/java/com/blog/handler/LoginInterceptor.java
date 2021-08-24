package com.blog.handler;

import com.alibaba.fastjson.JSON;
import com.blog.controller.vo.ErrorCode;
import com.blog.controller.vo.Result;
import com.blog.dao.pojo.SysUser;
import com.blog.service.LoginService;
import com.blog.utils.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: hu
 * @create: 2021-08-18 21:17
 * 对需要登录才能访问的接口进行验证拦截
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
  @Autowired
  private LoginService loginService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //在Controller方法之前执行

    /*
    * 1、需要判断请求接口路径是否为HandlerMethod（controller方法）
    * 2、判断token是否为空， 如果为空则判定未登录
    * 3、如果token不为空，登录验证 loginService checkToken
    * 4、如果认证成功 放行
    * */
    if(!(handler instanceof HandlerMethod)){
      //handler 可能是 RequestResourceHandler springboot程序访问静态资源，默认到classpath下的static目录去查询
      return true;
    }
    String token = request.getHeader("Authorization");
    log.info("============ request start ===============");
    String requestURI = request.getRequestURI();
    log.info("request uri:{}",requestURI);
    log.info("request method:{}", request.getMethod());
    log.info("token:{}", token);
    log.info("============= request end ============");

    if(StringUtils.isBlank(token)){
      Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().print(JSON.toJSONString(result));
      return false;
    }
    SysUser sysUser = loginService.checkToken(token);
    if(sysUser == null){
      Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().print(JSON.toJSONString(result));
      return false;
    }

    //使用ThreadLocal来保存用户的信息，在其他地方都可以使用
    UserThreadLocalUtils.put(sysUser);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    //ThreadLocal保存的用户信息在这里要进行销毁，不然会造成内存泄漏的问题（不会再继续使用了，但是JVM无法清理）
    UserThreadLocalUtils.remove();
  }
}
