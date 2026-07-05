package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预算统计。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "预算统计")
public class BudgetVO {

  @Schema(description = "总预算")
  private BigDecimal totalBudget;

  @Schema(description = "已花费")
  private BigDecimal totalSpent;

  @Schema(description = "剩余(正)/超支(负)")
  private BigDecimal remaining;

  @Schema(description = "是否超支")
  private boolean overspent;

  @Schema(description = "按品类分布")
  private List<BudgetGroupVO> byCategory;

  @Schema(description = "按空间分布")
  private List<BudgetGroupVO> bySpace;
}
