package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 品牌基础信息。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_brand")
public class Brand extends BaseEntity {

  /** 品牌名称 */
  private String name;

  /** 品牌LOGO */
  private String logo;

  /** domestic国产 / imported进口 */
  private String origin;

  /** high高端 / mid中端 / entry入门 */
  private String tier;

  /** 主营品类ID集合(逗号分隔) */
  private String mainCategoryIds;

  /** 价格区间下限 */
  private BigDecimal priceMin;

  /** 价格区间上限 */
  private BigDecimal priceMax;

  /** 售后服务说明 */
  private String afterSales;

  /** 官方购买渠道 */
  private String officialChannel;

  /** 好评率% */
  private BigDecimal praiseRate;

  /** 踩坑反馈条数 */
  private Integer pitfallCount;
}
