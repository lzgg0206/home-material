package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 全局搜索结果（按品类/品牌/型号分类）。
 */
@Data
@Schema(description = "全局搜索结果")
public class SearchVO {

  @Schema(description = "匹配品类")
  private List<SearchItemVO> categories;

  @Schema(description = "匹配品牌")
  private List<SearchItemVO> brands;

  @Schema(description = "匹配型号")
  private List<SearchItemVO> models;
}
