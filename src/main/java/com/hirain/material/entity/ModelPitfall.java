package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 型号踩坑点。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model_pitfall")
public class ModelPitfall extends BaseEntity {

  private Long modelId;

  /** quality质量 / install安装售后 / mismatch宣传不符 / experience使用体验 */
  private String type;

  private String description;

  private Integer count;

  private Integer isHighRisk;

  private String advice;
}
