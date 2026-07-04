package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 首页聚合结果。
 */
@Data
@Schema(description = "首页聚合")
public class HomeVO {

  @Schema(description = "快捷品类入口（高频二级品类）")
  private List<CategoryTreeVO> quickCategories;

  @Schema(description = "核心品类口碑榜单（各品类 TOP3）")
  private List<CategoryRanking> topRankings;

  @Schema(description = "今日避坑精选")
  private List<HomePitfallVO> dailyPitfalls;
}
