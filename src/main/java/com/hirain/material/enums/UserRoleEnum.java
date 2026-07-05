package com.hirain.material.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举。
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {

  USER(0, "普通用户"),
  ADMIN(1, "管理员");

  private final Integer code;
  private final String description;

  /**
   * 判断给定 code 是否为管理员（null 安全）。
   *
   * @param code 角色 code
   * @return true 表示管理员
   */
  public static boolean isAdmin(Integer code) {
    return ADMIN.code.equals(code);
  }
}
