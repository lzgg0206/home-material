package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 热门搜索词。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_hot_keyword")
public class HotKeyword extends BaseEntity {

  /** 热搜词 */
  private String keyword;

  /** 搜索次数 */
  private Integer searchCount;
}
