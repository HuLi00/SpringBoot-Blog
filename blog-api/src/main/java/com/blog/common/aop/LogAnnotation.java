package com.blog.common.aop;

import java.lang.annotation.*;
//自定义一个注解，其实注解就相当于一个标识
// 当框架识别到这个标识之后去做相应的处理操作
@Target({ElementType.TYPE,ElementType.METHOD})  //表示这个注解可以加到类上，也可以加到方法上
@Retention(RetentionPolicy.RUNTIME) //这个是保留策略，表示在运行时保留
@Documented
public @interface LogAnnotation {
  String module() default "";
  String operator() default "";
}
