package com.blog.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.controller.vo.*;
import com.blog.controller.vo.params.ArticleBodyParam;
import com.blog.controller.vo.params.ArticlePublishParams;
import com.blog.controller.vo.params.PageParams;
import com.blog.dao.dos.Archives;
import com.blog.dao.mapper.ArticleBodyMapper;
import com.blog.dao.mapper.ArticleMapper;
import com.blog.dao.mapper.ArticleTagMapper;
import com.blog.dao.pojo.Article;
import com.blog.dao.pojo.ArticleBody;
import com.blog.dao.pojo.ArticleTag;
import com.blog.dao.pojo.SysUser;
import com.blog.service.*;
import com.blog.utils.UserThreadLocalUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: hu
 * @create: 2021-08-17 16:12
 */
@Service
public class ArticleServiceImpl implements ArticleService {
  @Autowired
  private ArticleMapper articleMapper;
  @Autowired
  private TagService tagService;
  @Autowired
  private SysUserService sysUserService;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private ThreadPoolService threadPoolService;
  @Autowired
  private ArticleTagMapper articleTagMapper;

  public Result listArticle(PageParams pageParams) {
    /*根据文章categoryId和TagId还有年月进行文章分类*/
    //mybatisPlus中的Page
    Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
    IPage<Article> articleIPage = articleMapper.listArticle(page,
            pageParams.getCategoryId(),
            pageParams.getTagId(),
            pageParams.getYear(),
            pageParams.getMonth());
    List<Article> records = articleIPage.getRecords();
    return Result.success(copyList(records, true, true));
  }

  /*@Override
  public Result listArticle(PageParams pageParams) {
    *//*分页查询数据库article表*//*

    //mybatisPlus中的Page
    Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
    //查询条件
    LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
    //根据categoryId进行分类
    if(pageParams.getCategoryId() != null){
      queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
    }
    //先根据是否指定进行排序
//    queryWrapper.orderByDesc(Article::getWeight);
    //order by create_date;
    queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
    Page<Article> articlePage = articleMapper.selectPage(page,queryWrapper);
    List<Article> records = articlePage.getRecords();

    //将在数据库中查询道德数据进行一次转换，转换成前端页面需要的数据，减少传输的数据量
    List<ArticleVo> articleVoList = copyList(records, true, true);
    return Result.success(articleVoList);
  }*/

  private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
    //将数据库查询后的Article List转成 ArticleVo List
    List<ArticleVo> articleVoList = new ArrayList<>();
    for (Article record : records) {
      articleVoList.add(copy(record, isTag, isAuthor, false, false));
    }
    return articleVoList;
  }

  private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
    //将数据库查询后的Article List转成 ArticleVo List
    List<ArticleVo> articleVoList = new ArrayList<>();
    for (Article record : records) {
      articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
    }
    return articleVoList;
  }

  private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, Boolean isBody, Boolean isCategory){
    //创建一个articleVo对象，将数据库中查询到的article对象中的属性复制到article对象的属性中
    ArticleVo articleVo = new ArticleVo();
    //赋值属性
    BeanUtils.copyProperties(article, articleVo);
    articleVo.setId(String.valueOf(article.getId()));
    //将属性不同的字段做一下转换处理
    articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

    //添加博客标签和作者信息
    if(isTag){
      Long articleId = article.getId();
      articleVo.setTags(tagService.findTagsByArticleId(articleId));
    }
    if(isAuthor){
      long userId = article.getAuthorId();
      SysUser sysUser = sysUserService.findUserNameByAuthorId(userId);
      articleVo.setAuthor(sysUser.getNickname());
    }
    if(isBody){
      Long bodyId = article.getBodyId();
      articleVo.setBody(findArticleBodyByBodyId(bodyId));
    }
    if(isCategory){
      Long categoryId = article.getCategoryId();
      articleVo.setCategory(categoryService.findCategoryByCategoryId(categoryId));
    }
    return articleVo;
  }

  /*根据浏览量返回最热文章*/
  @Override
  public Result getHotArticles(int limit) {
    LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.orderByDesc(Article::getViewCounts);
    lambdaQueryWrapper.select(Article::getId, Article::getTitle);
    lambdaQueryWrapper.last("limit " + limit);
    //以上相当于sql语句 select id, title from ms_articles order by view_count desc limit 5;
    List<Article> articles = articleMapper.selectList(lambdaQueryWrapper);
    return Result.success(copyList(articles, false, false));
  }


  /*跟据文章创建时间返回最新文章*/
  @Override
  public Result getNewArticle(int limit) {
    LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.select(Article::getId, Article::getTitle);
    queryWrapper.orderByDesc(Article::getCreateDate);
    queryWrapper.last("limit "+ limit);
    List<Article> articles = articleMapper.selectList(queryWrapper);
    return Result.success(copyList(articles, false, false));
  }

  /*文章归档*/
  @Override
  public Result listArchives() {
    /*
    * sql语句
    * SELECT year(FROM_UNIXTIME(create_date/1000)) as year, month(FROM_UNIXTIME(create_date/1000)) AS month, count(*) as count from ms_article GROUP BY year, month;
    * */
    List<Archives> archivesList = articleMapper.listArchives();
    return Result.success(archivesList);
  }

  /*文章详情*/
  @Override
  public Result findArticleById(Long id) {
    /*
    * 1、根据id查询 文章信息
    * 2、根据bodyId和categoryid 去做关联查询
    * */
    Article article = articleMapper.selectById(id);
    ArticleVo articleVo = copy(article, true, true, true, true);
    /*
    * 当查看完文章后，应该将数据库中的浏览量+1，
    * 但是如果每次都在这里进行数据库的浏览量的更新，那么会应该这个接口的执行时间，
    * 这里用一下线程池的技术
    * */
    threadPoolService.updateArticleViewCount(articleMapper, article);
    return Result.success(articleVo);
  }

  @Autowired
  private ArticleBodyMapper articleBodyMapper;

  public ArticleBodyVo findArticleBodyByBodyId(Long id){
    ArticleBody articleBody = articleBodyMapper.selectById(id);
    ArticleBodyVo articleBodyVo = new ArticleBodyVo();
    articleBodyVo.setContent(articleBody.getContent());
    return  articleBodyVo;
  }

  /**
  *
  *@Param: [articlePublishParams]
  *@return: com.blog.controller.vo.Result
  *@Author: Hu li
  *发布文章
  */
  @Override
  @Transactional
  public Result publishArticle(ArticlePublishParams articlePublishParams) {
    /*
    用户的Id要从ThreadLocal中获取，使用拦截器拦截此接口路由可以获得user
    * 1、将article存入article数据表中
    * 2、将articleId与tagid做关联
    * 3、将body存入articlebody中
    * 4、以上操作要保证原子性，所以要加入事务机制
    * */
    //将article存入article数据表中
    SysUser user = UserThreadLocalUtils.get();
    Article article = new Article();
    article.setAuthorId(user.getId());
    article.setSummary(articlePublishParams.getSummary());
    article.setCommentCounts(0);
    article.setCreateDate(System.currentTimeMillis());
    article.setTitle(articlePublishParams.getTitle());
    article.setWeight(0);
    article.setCategoryId(Long.parseLong(articlePublishParams.getCategory().getId()));
    article.setViewCounts(0);
    //这里进行插入操作后，当插入完成后会将id值自动注入到article实体类中
    articleMapper.insert(article);
    //关联article表和article_tag表
    Long articleId = article.getId();
    List<TagVo> tags = articlePublishParams.getTags();
    if(tags != null){
      for (TagVo tagVo : tags) {
        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(articleId);
        articleTag.setTagId(Long.parseLong(tagVo.getId()));
        articleTagMapper.insert(articleTag);
      }
    }
    //将body存入articlebody中
    ArticleBody articleBody = new ArticleBody();
    articleBody.setArticleId(articleId);
    articleBody.setContent(articlePublishParams.getBody().getContent());
    articleBody.setContentHtml(articlePublishParams.getBody().getContentHtml());
    articleBodyMapper.insert(articleBody);
    //将存入article数据表中的数据做更新
    article.setBodyId(articleBody.getId());
    LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(Article::getId, articleId);
    articleMapper.update(article, updateWrapper);
    //返回内容
    Map<String, String> map = new HashMap<>();
    map.put("id", articleId.toString());
    return Result.success(map);
  }
}
