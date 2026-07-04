package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 口碑关键词。
 */
@Data
@Schema(description = "口碑关键词")
public class KeywordVO {

  @Schema(description = "关键词")
  private String keyword;

  @Schema(description = "情感 1正面/2负面")
  private Integer sentiment;

  @Schema(description = "提及频次")
  private Integer mentionCount;
}
