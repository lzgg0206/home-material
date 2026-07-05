package com.hirain.material.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 踩坑类型枚举。
 *
 * @author lingzhi.Wang
 */
@Getter
@AllArgsConstructor
public enum PitfallTypeEnum {

  QUALITY("quality", "质量问题"),
  INSTALL("install", "安装售后"),
  MISMATCH("mismatch", "宣传不符"),
  EXPERIENCE("experience", "使用体验"),
  OTHER("other", "其他");

  /** 类型编码 */
  private final String code;
  /** 类型描述 */
  private final String description;

  /**
   * 根据 code 解析枚举，未匹配或 null 一律返回 OTHER。
   *
   * @param code 类型编码
   * @return 对应枚举
   */
  public static PitfallTypeEnum fromCode(String code) {
    if (code == null) {
      return OTHER;
    }
    for (PitfallTypeEnum e : values()) {
      if (e.code.equals(code)) {
        return e;
      }
    }
    return OTHER;
  }
}
