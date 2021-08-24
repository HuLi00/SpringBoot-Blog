package com.blog.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.blog.controller.vo.ErrorCode;
import com.blog.controller.vo.LoginUserVo;
import com.blog.controller.vo.Result;
import com.blog.controller.vo.params.UserLoginParams;
import com.blog.dao.pojo.SysUser;
import com.blog.service.LoginService;
import com.blog.service.SysUserService;
import com.blog.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: hu
 * @create: 2021-08-18 15:34
 */
@Service
@Transactional  //加入事务机制
public class LoginServiceImpl implements LoginService {
  private static final String salt = "1688888DoReMi!@#$$";
  @Autowired
  private SysUserService sysUserService;
  //导入Redis
  @Autowired
  private RedisTemplate<String,String> redisTemplate;

  @Override
  public Result login(UserLoginParams userLoginParams) {
    /*登录步骤：
    * 1、检查参数是否合法
    * 2、根据用户名和密码去user表中查询是否存在
    * 3、如果不存在 登录失败
    * 4、如果存在  使用JWT 生成Token 返回给前端
    * 5、将token值放入redis中， redis  token：user信息 设置过期时间
    * （登录认证的时候 先认证token字符串是否合法 再去redis认证是否存在）
    * */
    String userAccount = userLoginParams.getAccount();
    String userPassword = userLoginParams.getPassword();
    //1、检查参数是否合法
    if(StringUtils.isBlank(userAccount) || StringUtils.isBlank(userPassword)){
      return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }
    //密码经过md5加密
    userPassword = DigestUtils.md5Hex(userPassword + salt);
    //2、根据用户名和密码去user表中查询是否存在
    SysUser user = sysUserService.findUser(userAccount,userPassword);
    //3、如果不存在 登录失败
    if(user == null) return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
    //4、如果存在  使用JWT 生成Token 返回给前端
    String token = JWTUtils.createToken(user.getId());
    //5、将token值放入redis中， redis  token：user信息 设置过期时间
    redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
    return Result.success(token);
  }

  @Override
  public SysUser checkToken(String token) {
    if(StringUtils.isBlank(token)){
      return null;
    }
    Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
    if(stringObjectMap == null){
      return null;
    }
    String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
    if(StringUtils.isBlank(userJson)){
      return null;
    }
    SysUser user = JSON.parseObject(userJson, SysUser.class);
    return user;
  }

  @Override
  public Result logout(String token) {
    /*
    * 直接删除redis中的token值就行了
    * */
    redisTemplate.delete("TOKEN_"+token);
    return Result.success(null);
  }

  @Override
  public Result register(UserLoginParams registerParam) {
    /*注册步骤：
    * 1、校验参数是否合法
    * 2、判断账户是否存在
    * 3、不存在，注册用户
    * 4、生成Token
    * 5、存入redis，并返回
    * 6、加上事务，注册过程中任何一步出现问题，注册的用户需要回滚
    * */

    //1、校验参数是否合法
    String userAccount = registerParam.getAccount();
    String userPassword = registerParam.getPassword();
    String userNickname = registerParam.getNickname();
    if(StringUtils.isBlank(userAccount)
            || StringUtils.isBlank(userPassword)
            || StringUtils.isBlank(userNickname) ){
      return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }
    //2、判断用户是否已经存在
    SysUser user = sysUserService.findUserByAccount(userAccount);
    if(user != null){
      return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
    }
    //3、注册用户
    user = new SysUser();
    user.setNickname(userNickname);
    user.setAccount(userAccount);
    user.setPassword(DigestUtils.md5Hex(userPassword+salt));
    user.setCreateDate(System.currentTimeMillis());
    user.setLastLogin(System.currentTimeMillis());
    user.setAdmin(0); //1为true，0为false
    user.setAvatar("http://localhost:8080/static/user/user1.png");
    user.setDeleted(0);
    user.setSalt("");
    user.setStatus("");
    user.setEmail("");
    sysUserService.registerUser(user);
    //4、生成Token
    String token = JWTUtils.createToken(user.getId());
    //存入redis
    redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user), 1, TimeUnit.DAYS);
    return Result.success(token);
  }
}
