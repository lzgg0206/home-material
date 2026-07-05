package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 踩坑分组（按类型）。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "踩坑分组")
public class PitfallGroupVO {

  @Schema(description = "类型")
  private String type;

  @Schema(description = "类型中文名")
  private String typeName;

  @Schema(description = "踩坑列表（高危已置顶）")
  private List<PitfallVO> items;
}
