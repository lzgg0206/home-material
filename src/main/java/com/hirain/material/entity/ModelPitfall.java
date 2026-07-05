package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 型号踩坑点。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model_pitfall")
public class ModelPitfall extends BaseEntity {

  /** 型号ID */
  private Long modelId;

  /** quality质量 / install安装售后 / mismatch宣传不符 / experience使用体验 */
  private String type;

  /** 坑点描述 */
  private String description;

  /** 出现频次 */
  private Integer count;

  /** 是否高危(0/1) */
  private Integer isHighRisk;

  /** 避坑建议 */
  private String advice;
}
