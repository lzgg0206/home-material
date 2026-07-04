package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 搜索结果项。
 */
@Data
@Schema(description = "搜索结果项")
public class SearchItemVO {

  @Schema(description = "ID")
  private Long id;

  @Schema(description = "名称")
  private String name;

  @Schema(description = "类型 category/brand/model")
  private String type;
}
