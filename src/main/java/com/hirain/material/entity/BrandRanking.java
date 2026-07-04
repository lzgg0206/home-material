package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 品牌排行榜缓存（首版动态算，此表可选）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_brand_ranking")
public class BrandRanking extends BaseEntity {

  private Long categoryId;

  private Long brandId;

  /** overall综合 / cost性价比 / highend高端 / lowpitfall低踩坑 / eco环保 */
  private String dimension;

  private Integer rank;

  private BigDecimal score;
}
