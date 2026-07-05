package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_user")
public class User extends BaseEntity {

  /** 微信openid */
  private String openid;

  /** 昵称 */
  private String nickname;

  /** 手机号 */
  private String phone;

  /** 头像URL */
  private String avatar;

  /** 角色：0普通用户 1管理员 */
  private Integer role;
}
