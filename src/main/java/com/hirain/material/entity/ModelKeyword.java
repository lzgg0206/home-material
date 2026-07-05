package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 型号口碑关键词。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model_keyword")
public class ModelKeyword extends BaseEntity {

  /** 型号ID */
  private Long modelId;

  /** 口碑关键词 */
  private String keyword;

  /** 1正面 / 2负面 */
  private Integer sentiment;

  /** 提及频次 */
  private Integer mentionCount;
}
