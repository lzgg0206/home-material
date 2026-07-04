package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 清单项。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_selection_item")
public class SelectionItem extends BaseEntity {

  private Long listId;

  private Long modelId;

  private Long categoryId;

  private String space;

  private String spec;

  private Integer quantity;

  private BigDecimal unitPrice;

  private BigDecimal totalPrice;

  private String channel;

  private String remark;

  /** 待采购pending / 已下单ordered / 已收货received */
  private String purchaseStatus;
}
