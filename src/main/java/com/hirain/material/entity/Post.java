package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 小红书内容源（Mock）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_post")
public class Post extends BaseEntity {

  private String sourceId;

  private String account;

  private String content;

  private Integer likeCnt;

  private Integer collectCnt;

  private Integer commentCnt;

  private Integer isAd;

  private BigDecimal qualityScore;

  private LocalDateTime crawlTime;
}
