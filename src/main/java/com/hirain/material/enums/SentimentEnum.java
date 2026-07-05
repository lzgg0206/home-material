package com.hirain.material.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关键词情感倾向枚举。
 *
 * @author lingzhi.Wang
 */
@Getter
@AllArgsConstructor
public enum SentimentEnum {

  POSITIVE(1, "正面"),
  NEGATIVE(2, "负面");

  /** 情感编码 */
  private final Integer code;
  /** 情感描述 */
  private final String description;
}
