package com.hirain.material.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Mock 登录请求（P0 联调用）。
 */
@Data
@Schema(description = "Mock 登录请求")
public class LoginRequest {

  @Schema(description = "微信openid（联调可任意填）", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "openid 不能为空")
  private String openid;
}
