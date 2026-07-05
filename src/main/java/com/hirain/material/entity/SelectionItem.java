package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 清单项。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_selection_item")
public class SelectionItem extends BaseEntity {

  /** 所属清单ID */
  private Long listId;

  /** 型号ID */
  private Long modelId;

  /** 品类ID */
  private Long categoryId;

  /** 空间 */
  private String space;

  /** 规格 */
  private String spec;

  /** 数量 */
  private Integer quantity;

  /** 单价 */
  private BigDecimal unitPrice;

  /** 小计金额 */
  private BigDecimal totalPrice;

  /** 购买渠道 */
  private String channel;

  /** 备注 */
  private String remark;

  /** 待采购pending / 已下单ordered / 已收货received */
  private String purchaseStatus;
}
