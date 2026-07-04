package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 清单项（含型号/品类名）。
 */
@Data
@Schema(description = "清单项")
public class SelectionItemVO {

  @Schema(description = "ID")
  private Long id;

  @Schema(description = "清单ID")
  private Long listId;

  @Schema(description = "型号ID")
  private Long modelId;

  @Schema(description = "型号名称")
  private String modelName;

  @Schema(description = "品类ID")
  private Long categoryId;

  @Schema(description = "品类名称")
  private String categoryName;

  @Schema(description = "空间")
  private String space;

  @Schema(description = "规格")
  private String spec;

  @Schema(description = "数量")
  private Integer quantity;

  @Schema(description = "单价")
  private BigDecimal unitPrice;

  @Schema(description = "小计")
  private BigDecimal totalPrice;

  @Schema(description = "购买渠道")
  private String channel;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "采购状态 pending/ordered/received")
  private String purchaseStatus;
}
