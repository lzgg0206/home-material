package com.hirain.material.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 大模型返回的单个品牌分析项（反序列化 LLM JSON 用）。
 *
 * @author lingzhi.Wang
 */
@Data
public class AiBrandDTO {

  /** 品牌名称 */
  private String name;

  /** 产地 domestic/imported */
  private String origin;

  /** 定位 high/mid/entry */
  private String tier;

  /** 好评率% */
  private BigDecimal praiseRate;

  /** 踩坑反馈条数 */
  private Integer pitfallCount;

  /** 均价区间下限 */
  private BigDecimal priceMin;

  /** 均价区间上限 */
  private BigDecimal priceMax;

  /** 核心标签 */
  private List<String> tags;

  /** 小红书口碑亮点（一句话） */
  private String highlight;
}
