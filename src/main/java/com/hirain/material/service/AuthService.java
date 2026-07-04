package com.hirain.material.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.entity.User;
import com.hirain.material.mapper.UserMapper;
import com.hirain.material.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证业务：Mock 登录 + 微信登录 + 当前用户。
 */
@Service
public class AuthService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private WxLoginService wxLoginService;

  /**
   * Mock 登录：openid 换 token（不存在则建用户）。
   *
   * @param openid 微信 openid
   * @return 登录响应
   */
  @Transactional
  public LoginVO login(String openid) {
    User user = getOrCreate(openid);
    StpUtil.login(user.getId());
    return toVO(user);
  }

  /**
   * 微信登录：code → code2session → openid → token。
   *
   * @param code wx.login() 拿到的 code
   * @return 登录响应
   */
  public LoginVO wxLogin(String code) {
    return login(wxLoginService.code2session(code));
  }

  /**
   * 获取当前登录用户。
   *
   * @return 当前用户
   */
  public User currentUser() {
    long uid = StpUtil.getLoginIdAsLong();
    return userMapper.selectById(uid);
  }

  private User getOrCreate(String openid) {
    User user = userMapper.selectOne(
        Wrappers.<User>lambdaQuery().eq(User::getOpenid, openid));
    if (user == null) {
      user = new User();
      user.setOpenid(openid);
      String suffix = openid.length() > 4 ? openid.substring(openid.length() - 4) : openid;
      user.setNickname("业主" + suffix);
      userMapper.insert(user);
    }
    return user;
  }

  private LoginVO toVO(User u) {
    LoginVO vo = new LoginVO();
    vo.setToken(StpUtil.getTokenValue());
    vo.setUserId(u.getId());
    vo.setNickname(u.getNickname());
    vo.setAvatar(u.getAvatar());
    return vo;
  }
}
