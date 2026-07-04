package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 热门搜索词。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_hot_keyword")
public class HotKeyword extends BaseEntity {

  private String keyword;

  private Integer searchCount;
}
