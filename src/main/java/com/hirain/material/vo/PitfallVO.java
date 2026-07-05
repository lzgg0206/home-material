package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 踩坑点。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "踩坑点")
public class PitfallVO {

  @Schema(description = "ID")
  private Long id;

  @Schema(description = "类型 quality/install/mismatch/experience")
  private String type;

  @Schema(description = "类型中文名")
  private String typeName;

  @Schema(description = "坑点描述")
  private String description;

  @Schema(description = "出现频次")
  private Integer count;

  @Schema(description = "是否高危 0/1")
  private Integer isHighRisk;

  @Schema(description = "避坑建议")
  private String advice;
}
