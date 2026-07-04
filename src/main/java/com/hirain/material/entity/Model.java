package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 型号信息。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model")
public class Model extends BaseEntity {

  private Long brandId;

  private Long categoryId;

  private String name;

  private String spec;

  private BigDecimal price;

  private String ecoLevel;

  /** 核心卖点标签(逗号分隔) */
  private String sellingPoints;
}
