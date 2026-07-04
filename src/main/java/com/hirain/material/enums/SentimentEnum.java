package com.hirain.material.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关键词情感倾向枚举。
 */
@Getter
@AllArgsConstructor
public enum SentimentEnum {

  POSITIVE(1, "正面"),
  NEGATIVE(2, "负面");

  private final Integer code;
  private final String description;
}
