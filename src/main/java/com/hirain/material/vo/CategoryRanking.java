package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 首页品类榜单（某品类下 TOP3 品牌）。
 */
@Data
@Schema(description = "品类榜单")
public class CategoryRanking {

  @Schema(description = "品类ID")
  private Long categoryId;

  @Schema(description = "品类名称")
  private String categoryName;

  @Schema(description = "TOP3 品牌")
  private List<BrandRankingVO> top3;
}
