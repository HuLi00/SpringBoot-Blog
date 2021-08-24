package com.blog.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.controller.vo.ErrorCode;
import com.blog.controller.vo.LoginUserVo;
import com.blog.controller.vo.Result;
import com.blog.controller.vo.UserVo;
import com.blog.dao.mapper.SysUserMapper;
import com.blog.dao.pojo.SysUser;
import com.blog.service.LoginService;
import com.blog.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: hu
 * @create: 2021-08-17 23:15
 */
@Service
public class SysUserServiceImpl implements SysUserService {
  @Autowired
  private SysUserMapper sysUserMapper;
  @Autowired
  private LoginService loginService;

  @Override
  public SysUser findUserNameByAuthorId(long userId) {
    SysUser sysUser = sysUserMapper.selectById(userId);
    if(sysUser == null){
      sysUser = new SysUser();
      sysUser.setNickname("DoReMi");
    }
    return sysUser;
  }

  @Override
  public SysUser findUser(String userAccount, String userPassword) {
    LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SysUser::getAccount, userAccount);
    queryWrapper.eq(SysUser::getPassword,userPassword);
    queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
    queryWrapper.last("limit 1");
    return sysUserMapper.selectOne(queryWrapper);
  }

  @Override
  public Result findUserByToken(String token) {
    /*
    * 1、token合法性校验
    *   是否为空，Redis是否存在
    * 2、如果校验失败 返回结果
    * 3、如果成功，返回对应的结果LoginUserVo(从Redis中返回)
    * */
    SysUser user = loginService.checkToken(token);
    if(user == null){
      return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
    }
    LoginUserVo loginUserVo = new LoginUserVo();
    BeanUtils.copyProperties(user, loginUserVo);
    //因为缓存精度问题做的修改
    loginUserVo.setId(String.valueOf(user.getId()));
    return Result.success(loginUserVo);
  }

  @Override
  public SysUser findUserByAccount(String Account) {
    LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SysUser::getAccount, Account);
    queryWrapper.last("limit 1");
    return sysUserMapper.selectOne(queryWrapper);
  }

  @Override
  public void registerUser(SysUser user) {
    sysUserMapper.insert(user);
  }

  @Override
  public UserVo findUserByAuthorId(Long authorId) {
    SysUser user = sysUserMapper.selectById(authorId);
    if(user == null){
      user = new SysUser();
      user.setId(1L);
      user.setAvatar("http://localhost:8080/static/user/user_1.png");
      user.setNickname("DoReMi");
    }
    UserVo userVo = new UserVo();
    BeanUtils.copyProperties(user, userVo);
    userVo.setId(String.valueOf(user.getId()));
    return userVo;
  }
}
