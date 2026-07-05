package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 型号口碑聚合。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model_reputation")
public class ModelReputation extends BaseEntity {

  /** 型号ID */
  private Long modelId;

  /** 好评率% */
  private BigDecimal praiseRate;

  /** 踩坑率% */
  private BigDecimal pitfallRate;

  /** 有效样本数 */
  private Integer sampleCount;

  /** 质量评分 */
  private BigDecimal qualityScore;
}
