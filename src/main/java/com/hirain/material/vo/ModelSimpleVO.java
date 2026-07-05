package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 型号简要信息。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "型号简要")
public class ModelSimpleVO {

  @Schema(description = "型号ID")
  private Long id;

  @Schema(description = "型号名称")
  private String name;

  @Schema(description = "规格")
  private String spec;

  @Schema(description = "参考价")
  private BigDecimal price;

  @Schema(description = "环保等级")
  private String ecoLevel;

  @Schema(description = "好评率%")
  private BigDecimal praiseRate;

  @Schema(description = "踩坑率%")
  private BigDecimal pitfallRate;
}
