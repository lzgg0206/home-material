package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 家装品类（四级树，第 4 级即型号）。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_category")
public class Category extends BaseEntity {

  /** 父级品类ID */
  private Long parentId;

  /** 层级 */
  private Integer level;

  /** 品类名称 */
  private String name;

  /** 品类编码 */
  private String code;

  /** 图标 */
  private String icon;

  /** 排序 */
  private Integer sort;
}
