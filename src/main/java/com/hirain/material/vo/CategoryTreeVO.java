package com.hirain.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 品类树节点。
 */
@Data
@Schema(description = "品类树节点")
public class CategoryTreeVO {

  @Schema(description = "品类ID")
  private Long id;

  @Schema(description = "名称")
  private String name;

  @Schema(description = "编码")
  private String code;

  @Schema(description = "图标")
  private String icon;

  @Schema(description = "排序")
  private Integer sort;

  @Schema(description = "子级")
  private List<CategoryTreeVO> children;
}
