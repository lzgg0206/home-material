package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 型号信息。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_model")
public class Model extends BaseEntity {

  /** 所属品牌ID */
  private Long brandId;

  /** 所属品类ID */
  private Long categoryId;

  /** 型号名称 */
  private String name;

  /** 规格参数 */
  private String spec;

  /** 参考价 */
  private BigDecimal price;

  /** 环保等级 */
  private String ecoLevel;

  /** 核心卖点标签(逗号分隔) */
  private String sellingPoints;
}
