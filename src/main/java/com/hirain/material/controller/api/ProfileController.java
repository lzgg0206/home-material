package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.entity.UserProfile;
import com.hirain.material.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 家装档案接口（C 端，需登录）。
 * 提供当前用户家装档案的查询与保存，路径前缀 /api/profile。
 *
 * @author lingzhi.Wang
 */
@Tag(name = "家装档案", description = "用户家装档案")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

  @Autowired
  private ProfileService profileService;

  /**
   * 查询当前用户档案。
   *
   * @return 档案
   */
  @Operation(summary = "我的家装档案")
  @GetMapping
  public Result<UserProfile> get() {
    return Result.success(profileService.myProfile());
  }

  /**
   * 保存（新建或更新）档案。
   *
   * @param profile 档案内容
   * @return 保存后的档案
   */
  @Operation(summary = "保存家装档案")
  @PostMapping
  public Result<UserProfile> save(@RequestBody UserProfile profile) {
    return Result.success(profileService.save(profile));
  }
}
