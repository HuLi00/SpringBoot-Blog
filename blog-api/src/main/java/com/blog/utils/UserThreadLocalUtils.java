package com.blog.utils;

import com.blog.dao.pojo.SysUser;

/**
 * @author: hu
 * @create: 2021-08-19 14:13
 */
public class UserThreadLocalUtils {

  private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

  private UserThreadLocalUtils(){}

  public static void put(SysUser user){
    LOCAL.set(user);
  }

  public static SysUser get(){
    return LOCAL.get();
  }

  public static void remove(){
    LOCAL.remove();
  }
}
