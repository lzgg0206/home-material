package com.hirain.material.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.entity.UserProfile;
import com.hirain.material.mapper.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 家装档案业务（upsert）。
 */
@Service
public class ProfileService {

  @Autowired
  private UserProfileMapper profileMapper;

  @Autowired
  private AuthService authService;

  /**
   * 查询当前用户家装档案。
   *
   * @return 档案，未建档返回 null
   */
  public UserProfile myProfile() {
    Long uid = authService.currentUser().getId();
    return profileMapper.selectOne(
        Wrappers.<UserProfile>lambdaQuery().eq(UserProfile::getUserId, uid));
  }

  /**
   * 保存（不存在则新建，存在则更新）当前用户家装档案。
   *
   * @param profile 档案内容
   * @return 保存后的档案
   */
  public UserProfile save(UserProfile profile) {
    Long uid = authService.currentUser().getId();
    profile.setUserId(uid);
    UserProfile exist = profileMapper.selectOne(
        Wrappers.<UserProfile>lambdaQuery().eq(UserProfile::getUserId, uid));
    if (exist == null) {
      profileMapper.insert(profile);
    } else {
      profile.setId(exist.getId());
      profileMapper.updateById(profile);
    }
    return profile;
  }
}
