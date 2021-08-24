package com.blog.common.aop;

import com.alibaba.fastjson.JSON;
import com.blog.controller.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: hu
 * @create: 2021-08-24 14:03
 */
@Component
@Aspect
@Slf4j
public class CacheAspect {
  private static volatile Set<String> clearRedisKey = new HashSet<>();
  @Autowired
  private RedisTemplate<String,String> redisTemplate;

  //定义切入点
  @Pointcut("@annotation(com.blog.common.aop.Cache)")
  public void cachePt(){}

  @Pointcut("@annotation(com.blog.common.aop.ClearCache)")
  public void cpt(){}

  //通知方式，环绕通知
  @Around("cachePt()")
  public Object around(ProceedingJoinPoint pjp){
    try{
      Signature signature = pjp.getSignature();
      //获取类名
      String className = pjp.getTarget().getClass().getSimpleName();  //获取类名，反射机制
      //调用的方法名
      String methodName = signature.getName();

      Class[] parameterTypes = new Class[pjp.getArgs().length];
      Object[] args = pjp.getArgs();
      //参数
      String params = "";
      for (int i = 0; i < args.length; i++) {
        if(args[i] != null){
          params += JSON.toJSONString(args[i]);
          parameterTypes[i] = args[i].getClass();
        }else{
          parameterTypes[i] = null;
        }
      }
      if(StringUtils.isNotEmpty(params)){
        //对param进行加密，防止key过长，或者字符串转义而获取不到
        params = DigestUtils.md5Hex(params);
      }
      Method method = pjp.getSignature().getDeclaringType().getMethod(methodName,parameterTypes);
      //获取Cache注解
      Cache annotation = method.getAnnotation(Cache.class);
      //获取过期时间
      long expire = annotation.expire();
      //获取缓存名称
      String name = annotation.name();
      //查redis缓存
      String redisKey = name + "::" + className + "::" + methodName + "::" + params;
      if(methodName.equals("articles")){
        clearRedisKey.add(redisKey);
      }
      String redisValue = redisTemplate.opsForValue().get(redisKey);
      if(StringUtils.isNotEmpty(redisValue)){
        log.info("缓存中查询到：{},{}",className,methodName);
        return JSON.parseObject(redisValue, Result.class);
      }
      //拿到注解方法执行的结果放入缓存中。
      Object proceed = pjp.proceed();
      redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
      log.info("存入缓存中：{},{}", className, methodName);
      return proceed;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return Result.fail(-999,"系统错误");
  }

  //
  @Before("cpt()")
  public void clearCache(){
    redisTemplate.delete("new_article::ArticleController::newArticle::");
    redisTemplate.delete("hot_article::ArticleController::hotArticles::");
    Iterator<String> it = clearRedisKey.iterator();
    while(it.hasNext()){
      String redisKey = it.next();
      redisTemplate.delete(redisKey);
    }
  }
}
