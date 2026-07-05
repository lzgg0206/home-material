package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自选清单。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_selection_list")
public class SelectionList extends BaseEntity {

  /** 用户ID */
  private Long userId;

  /** 清单名称 */
  private String name;

  /** 空间 */
  private String space;
}
