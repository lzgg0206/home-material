package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 品牌排行榜项。
 */
@Data
@Schema(description = "品牌排行榜项")
public class BrandRankingVO {

  @Schema(description = "名次")
  private Integer rank;

  @Schema(description = "品牌ID")
  private Long brandId;

  @Schema(description = "品牌名称")
  private String name;

  @Schema(description = "logo")
  private String logo;

  @Schema(description = "产地")
  private String origin;

  @Schema(description = "定位")
  private String tier;

  @Schema(description = "好评率%")
  private BigDecimal praiseRate;

  @Schema(description = "踩坑反馈条数")
  private Integer pitfallCount;

  @Schema(description = "均价区间下限")
  private BigDecimal priceMin;

  @Schema(description = "均价区间上限")
  private BigDecimal priceMax;

  @Schema(description = "核心标签")
  private List<String> tags;
}
