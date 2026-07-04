package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.dto.LoginRequest;
import com.hirain.material.dto.WxLoginRequest;
import com.hirain.material.entity.User;
import com.hirain.material.service.AuthService;
import com.hirain.material.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口（C 端）。
 */
@Tag(name = "认证", description = "登录与当前用户")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  /**
   * Mock 登录（openid 换 token，P0 联调用）。
   *
   * @param req 登录请求
   * @return 登录响应
   */
  @Operation(summary = "Mock 登录(openid)")
  @PostMapping("/login")
  public Result<LoginVO> login(@RequestBody @Valid LoginRequest req) {
    return Result.success(authService.login(req.getOpenid()));
  }

  /**
   * 微信登录（code → code2session → token）。
   *
   * @param req 微信登录请求
   * @return 登录响应
   */
  @Operation(summary = "微信登录(code2session)")
  @PostMapping("/wx-login")
  public Result<LoginVO> wxLogin(@RequestBody @Valid WxLoginRequest req) {
    return Result.success(authService.wxLogin(req.getCode()));
  }

  /**
   * 当前登录用户。
   *
   * @return 当前用户
   */
  @Operation(summary = "当前用户")
  @GetMapping("/me")
  public Result<User> me() {
    return Result.success(authService.currentUser());
  }
}
