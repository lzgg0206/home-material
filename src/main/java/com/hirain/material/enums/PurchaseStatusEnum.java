package com.hirain.material.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 清单项采购状态枚举。
 *
 * @author lingzhi.Wang
 */
@Getter
@AllArgsConstructor
public enum PurchaseStatusEnum {

  PENDING("pending", "待采购"),
  ORDERED("ordered", "已下单"),
  RECEIVED("received", "已收货");

  /** 状态编码 */
  private final String code;
  /** 状态描述 */
  private final String description;

  /**
   * 根据 code 解析枚举，未匹配返回 null。
   *
   * @param code 状态编码
   * @return 对应枚举，未匹配返回 null
   */
  public static PurchaseStatusEnum fromCode(String code) {
    if (code == null) {
      return null;
    }
    for (PurchaseStatusEnum e : values()) {
      if (e.code.equals(code)) {
        return e;
      }
    }
    return null;
  }
}
