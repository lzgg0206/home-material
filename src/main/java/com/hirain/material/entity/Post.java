package com.hirain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hirain.material.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 小红书内容源（Mock）。
 *
 * @author lingzhi.Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hm_post")
public class Post extends BaseEntity {

  /** 内容源ID */
  private String sourceId;

  /** 来源账号 */
  private String account;

  /** 正文内容 */
  private String content;

  /** 点赞数 */
  private Integer likeCnt;

  /** 收藏数 */
  private Integer collectCnt;

  /** 评论数 */
  private Integer commentCnt;

  /** 是否广告(0/1) */
  private Integer isAd;

  /** 内容质量分 */
  private BigDecimal qualityScore;

  /** 抓取时间 */
  private LocalDateTime crawlTime;
}
