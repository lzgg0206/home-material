package com.hirain.material.service;

import com.hirain.material.config.AiProperties;
import com.hirain.material.config.CacheConfig;
import com.hirain.material.entity.Brand;
import com.hirain.material.mapper.BrandMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 数据聚合与缓存刷新调度器。
 *
 * <p>每日凌晨 3 点：按型号口碑与踩坑反馈重新聚合品牌好评率/踩坑数；
 * 每日 3 点 30 分：异步预热 AI 品牌榜单（默认城市 × 配置品类 × 全维度）。
 * 两个任务结束后均清空业务缓存，让首页榜单在下一请求时重新加载。</p>
 *
 * @author lingzhi.Wang
 */
@Slf4j
@Component
public class DataSyncScheduler {

  /** AI 榜单预热覆盖的全部维度。 */
  private static final List<String> AI_DIMENSIONS =
      List.of("overall", "cost", "highend", "lowpitfall", "eco");

  @Autowired
  private BrandMapper brandMapper;

  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private AiBrandService aiBrandService;

  @Autowired
  private AiProperties aiProperties;

  /**
   * 每日 03:00 聚合品牌统计并清缓存。
   */
  @Scheduled(cron = "0 0 3 * * ?")
  public void aggregateBrandStats() {
    List<Brand> brands = brandMapper.selectList(null);
    int updated = 0;
    for (Brand b : brands) {
      int pitfallCount = brandMapper.sumPitfallCount(b.getId());
      BigDecimal praise = brandMapper.avgPraiseRate(b.getId());
      b.setPitfallCount(pitfallCount);
      b.setPraiseRate(praise == null ? BigDecimal.ZERO : praise.setScale(2, RoundingMode.HALF_UP));
      brandMapper.updateById(b);
      updated++;
    }
    evictAll();
    log.info("[调度] 品牌统计聚合完成，更新 {} 个品牌，缓存已清空", updated);
  }

  /**
   * 每日 03:30 预热 AI 品牌榜单。Mock 模式跳过，完成后清 AI 榜单缓存。
   */
  @Scheduled(cron = "0 30 3 * * ?")
  public void preloadAiRanking() {
    if (aiProperties.isMock()) {
      log.info("[调度] AI Mock 模式，跳过大模型榜单预热");
      return;
    }
    String city = aiProperties.getDefaultCity();
    int total = 0;
    for (Long cid : aiProperties.getPreloadCategoryIds()) {
      for (String dim : AI_DIMENSIONS) {
        try {
          aiBrandService.sync(city, cid, dim);
          total++;
        } catch (Exception e) {
          log.error("[调度] AI 预热失败 cat={} dim={}", cid, dim, e);
        }
      }
    }
    clear(CacheConfig.AI_RANKING);
    log.info("[调度] AI 榜单预热完成 city={} 任务数={}", city, total);
  }

  /** 清空全部业务缓存（首页/品类树/品牌榜/AI 榜单）。 */
  private void evictAll() {
    clear(CacheConfig.HOME);
    clear(CacheConfig.CATEGORY_TREE);
    clear(CacheConfig.BRAND_RANKING);
    clear(CacheConfig.AI_RANKING);
  }

  private void clear(String name) {
    org.springframework.cache.Cache cache = cacheManager.getCache(name);
    if (cache != null) {
      cache.clear();
    }
  }
}
