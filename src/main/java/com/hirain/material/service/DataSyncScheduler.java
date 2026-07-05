package com.hirain.material.service;

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
 * <p>每日凌晨 3 点：按型号口碑与踩坑反馈重新聚合品牌好评率/踩坑数，
 * 并清空业务缓存让首页榜单在下一请求时重新加载。</p>
 *
 * <p>当前数据为 AI 生成的种子数据（静态），聚合结果幂等；
 * 未来接入 UGC 后，本任务会把用户提交的踩坑/口碑自动滚入品牌统计。</p>
 *
 * @author lingzhi.Wang
 */
@Slf4j
@Component
public class DataSyncScheduler {

  @Autowired
  private BrandMapper brandMapper;

  @Autowired
  private CacheManager cacheManager;

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

  /** 清空全部业务缓存（首页/品类树/品牌榜）。 */
  private void evictAll() {
    clear(CacheConfig.HOME);
    clear(CacheConfig.CATEGORY_TREE);
    clear(CacheConfig.BRAND_RANKING);
  }

  private void clear(String name) {
    org.springframework.cache.Cache cache = cacheManager.getCache(name);
    if (cache != null) {
      cache.clear();
    }
  }
}
