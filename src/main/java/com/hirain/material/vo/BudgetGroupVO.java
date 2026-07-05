package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预算分组统计项。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "预算分组统计")
public class BudgetGroupVO {

  @Schema(description = "分组名（品类名/空间名）")
  private String name;

  @Schema(description = "金额")
  private BigDecimal amount;

  @Schema(description = "占比%")
  private BigDecimal ratio;
}
