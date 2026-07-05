package com.hirain.material.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置（Caffeine）。
 *
 * <p>用于首页聚合、品类树、品牌排行榜等"读多写少"热点接口：
 * 写入后 30 分钟过期、单缓存上限 500 条，B 端数据变更后由定时任务与后台 evict 刷新。</p>
 *
 * @author lingzhi.Wang
 */
@Configuration
public class CacheConfig {

  /** 缓存名：首页聚合 */
  public static final String HOME = "home";

  /** 缓存名：品类树 */
  public static final String CATEGORY_TREE = "categoryTree";

  /** 缓存名：品牌排行榜 */
  public static final String BRAND_RANKING = "brandRanking";

  /**
   * Caffeine 缓存管理器：统一 TTL 与容量上限。
   *
   * @return 缓存管理器
   */
  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager manager = new CaffeineCacheManager(HOME, CATEGORY_TREE, BRAND_RANKING);
    manager.setCaffeine(Caffeine.newBuilder()
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .maximumSize(500));
    return manager;
  }
}
