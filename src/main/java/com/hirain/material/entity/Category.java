package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 家装品类（四级树，第 4 级即型号）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_category")
public class Category extends BaseEntity {

  private Long parentId;

  private Integer level;

  private String name;

  private String code;

  private String icon;

  private Integer sort;
}
