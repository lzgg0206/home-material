package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户家装档案。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_user_profile")
public class UserProfile extends BaseEntity {

  /** 用户ID */
  private Long userId;

  /** 所在城市 */
  private String city;

  /** 面积㎡ */
  private BigDecimal area;

  /** 户型 */
  private String layout;

  /** 房屋类型 */
  private String houseType;

  /** 装修阶段 */
  private String stage;

  /** 装修风格 */
  private String style;

  /** 装修方式 */
  private String decorateWay;

  /** 总预算 */
  private BigDecimal totalBudget;

  /** 核心偏好排序 */
  private String preference;
}
