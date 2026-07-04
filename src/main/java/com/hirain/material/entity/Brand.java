package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 品牌基础信息。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_brand")
public class Brand extends BaseEntity {

  private String name;

  private String logo;

  /** domestic国产 / imported进口 */
  private String origin;

  /** high高端 / mid中端 / entry入门 */
  private String tier;

  /** 主营品类ID集合(逗号分隔) */
  private String mainCategoryIds;

  private BigDecimal priceMin;

  private BigDecimal priceMax;

  private String afterSales;

  private String officialChannel;

  /** 好评率% */
  private BigDecimal praiseRate;

  private Integer pitfallCount;
}
