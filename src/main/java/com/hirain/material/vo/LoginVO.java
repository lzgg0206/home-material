package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应。
 */
@Data
@Schema(description = "登录响应")
public class LoginVO {

  @Schema(description = "业务 token，前端放 Authorization 头")
  private String token;

  @Schema(description = "用户ID")
  private Long userId;

  @Schema(description = "昵称")
  private String nickname;

  @Schema(description = "头像")
  private String avatar;
}
