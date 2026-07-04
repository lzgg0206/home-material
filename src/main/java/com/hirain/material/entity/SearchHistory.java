package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索历史。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_search_history")
public class SearchHistory extends BaseEntity {

  private Long userId;

  private String keyword;
}
