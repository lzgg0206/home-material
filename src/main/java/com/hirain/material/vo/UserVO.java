package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 当前用户信息（脱敏视图，不含 openid/角色等内部字段）。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "当前用户信息")
public class UserVO {

  @Schema(description = "用户ID")
  private Long id;

  @Schema(description = "昵称")
  private String nickname;

  @Schema(description = "手机号")
  private String phone;

  @Schema(description = "头像")
  private String avatar;
}
