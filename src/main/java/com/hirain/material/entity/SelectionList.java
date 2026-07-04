package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自选清单。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_selection_list")
public class SelectionList extends BaseEntity {

  private Long userId;

  private String name;

  private String space;
}
