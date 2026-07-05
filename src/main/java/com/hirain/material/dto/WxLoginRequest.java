package com.hirain.material.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "微信登录请求")
public class WxLoginRequest {

  @Schema(description = "wx.login() 拿到的 code", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "code 不能为空")
  private String code;
}
