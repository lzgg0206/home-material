package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户家装档案。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_user_profile")
public class UserProfile extends BaseEntity {

  private Long userId;

  private String city;

  /** 面积㎡ */
  private BigDecimal area;

  private String layout;

  private String houseType;

  private String stage;

  private String style;

  private String decorateWay;

  private BigDecimal totalBudget;

  /** 核心偏好排序 */
  private String preference;
}
