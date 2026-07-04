package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 型号口碑关键词。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model_keyword")
public class ModelKeyword extends BaseEntity {

  private Long modelId;

  private String keyword;

  /** 1正面 / 2负面 */
  private Integer sentiment;

  private Integer mentionCount;
}
