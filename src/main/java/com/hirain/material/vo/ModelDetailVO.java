package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 型号详情聚合（型号详情页一次返回）。
 */
@Data
@Schema(description = "型号详情")
public class ModelDetailVO {

  @Schema(description = "型号ID")
  private Long modelId;

  @Schema(description = "型号全称")
  private String modelName;

  @Schema(description = "规格参数")
  private String spec;

  @Schema(description = "参考价")
  private BigDecimal price;

  @Schema(description = "环保等级")
  private String ecoLevel;

  @Schema(description = "核心卖点标签")
  private List<String> sellingPoints;

  @Schema(description = "品牌ID")
  private Long brandId;

  @Schema(description = "品牌名称")
  private String brandName;

  @Schema(description = "品牌logo")
  private String brandLogo;

  @Schema(description = "好评率%")
  private BigDecimal praiseRate;

  @Schema(description = "踩坑率%")
  private BigDecimal pitfallRate;

  @Schema(description = "有效样本数")
  private Integer sampleCount;

  @Schema(description = "口碑关键词云")
  private List<KeywordVO> keywords;

  @Schema(description = "业主公认优点（正面关键词，按提及数降序）")
  private List<KeywordVO> pros;

  @Schema(description = "踩坑经验与避坑建议（按类型分组，高危置顶）")
  private List<PitfallGroupVO> pitfalls;

  @Schema(description = "同品牌其他型号")
  private List<ModelSimpleVO> relatedSameBrand;

  @Schema(description = "同价位竞品推荐")
  private List<ModelSimpleVO> relatedSamePrice;
}
