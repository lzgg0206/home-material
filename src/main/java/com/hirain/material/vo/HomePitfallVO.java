package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 首页避坑精选项。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "避坑精选项")
public class HomePitfallVO {

  @Schema(description = "型号ID")
  private Long modelId;

  @Schema(description = "型号名称")
  private String modelName;

  @Schema(description = "品类标签")
  private String categoryName;

  @Schema(description = "坑点摘要")
  private String description;

  @Schema(description = "反馈频次")
  private Integer count;
}
