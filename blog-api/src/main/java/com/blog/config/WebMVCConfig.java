package com.blog.config;

import com.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: hu
 * @create: 2021-08-17 15:59
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
  @Autowired
  private LoginInterceptor loginInterceptor;

  @Override
  //跨域配置
  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    registry.addMapping("/**").allowedOrigins("http://localhost:8080");
  }

  //拦截器配置
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginInterceptor)
            .addPathPatterns("/test")
            .addPathPatterns("/comments/create/change")
            .addPathPatterns("/articles/publish");
  }
}
