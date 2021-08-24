package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.blog.dao.mapper.ArticleMapper;
import com.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author: hu
 * @create: 2021-08-19 22:00
 */
@Component
public class ThreadPoolService {

  //线程池任务操作
  @Async("taskExecutor")
  public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
//    try {
//      Thread.sleep(5000);
//      System.out.println("线程池成功执行，更新完成");
//    }catch (InterruptedException e){
//      e.printStackTrace();
//    }
    int viewCount = article.getViewCounts();
    Article articleUpdate = new Article();
    articleUpdate.setViewCounts(viewCount + 1);
    LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(Article::getId, article.getId());
    //下面这句话就相当于是乐观锁机制，保证在多线程环境下数据库更新的正确性。
    updateWrapper.eq(Article::getViewCounts, viewCount);
    articleMapper.update(articleUpdate, updateWrapper);
  }
}
