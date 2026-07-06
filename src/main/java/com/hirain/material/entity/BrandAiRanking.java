package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI 品牌榜单快照（按 城市×品类×维度 落库）。
 *
 * <p>大模型生成的品牌列表以 JSON 整体存 {@code brands_json}，
 * 由 {@code AiBrandService} 异步预热写入，{@code BrandService} 读库读缓存。</p>
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_brand_ai_ranking")
public class BrandAiRanking extends BaseEntity {

  /** 城市（"全国"为兜底） */
  private String city;

  /** 品类ID */
  private Long categoryId;

  /** 榜单维度 overall/cost/highend/lowpitfall/eco */
  private String dimension;

  /** LLM 生成的品牌列表 JSON（BrandRankingVO 数组，{brands:[...]} 结构） */
  private String brandsJson;

  /** 生成时使用的模型名 */
  private String model;
}
