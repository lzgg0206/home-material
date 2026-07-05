package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 品牌排行榜缓存（首版动态算，此表可选）。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_brand_ranking")
public class BrandRanking extends BaseEntity {

  /** 品类ID */
  private Long categoryId;

  /** 品牌ID */
  private Long brandId;

  /** overall综合 / cost性价比 / highend高端 / lowpitfall低踩坑 / eco环保 */
  private String dimension;

  /** 名次 */
  private Integer rank;

  /** 综合评分 */
  private BigDecimal score;
}
