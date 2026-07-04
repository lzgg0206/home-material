package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 型号口碑聚合。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model_reputation")
public class ModelReputation extends BaseEntity {

  private Long modelId;

  private BigDecimal praiseRate;

  private BigDecimal pitfallRate;

  private Integer sampleCount;

  private BigDecimal qualityScore;
}
