package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_user")
public class User extends BaseEntity {

  private String openid;

  private String nickname;

  private String phone;

  private String avatar;
}
