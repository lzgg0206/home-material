package com.hirain.material.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.entity.Brand;
import com.hirain.material.entity.Category;
import com.hirain.material.entity.Model;
import com.hirain.material.entity.ModelPitfall;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.CategoryMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.ModelPitfallMapper;
import com.hirain.material.vo.BrandRankingVO;
import com.hirain.material.vo.CategoryRanking;
import com.hirain.material.vo.CategoryTreeVO;
import com.hirain.material.vo.HomePitfallVO;
import com.hirain.material.vo.HomeVO;
import com.hirain.material.config.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 首页聚合业务：快捷品类 + 核心榜单 + 避坑精选。
 *
 * @author lingzhi.Wang
 */
@Service
public class HomeService {

  /** 首页核心榜单展示的品类ID（热门二级品类，覆盖硬装/厨卫/软装；顺序即展示顺序）。 */
  private static final List<Long> HOME_CATEGORY_IDS = List.of(10L, 13L, 16L);

  /** 每个品类展示的 TOP N 品牌。 */
  private static final int HOME_TOP_N = 3;

  /** 今日避坑精选条数。 */
  private static final int HOME_PITFALL_N = 3;

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private BrandMapper brandMapper;

  @Autowired
  private ModelPitfallMapper pitfallMapper;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * 首页聚合。
   *
   * @return 首页数据
   */
  @Cacheable(value = CacheConfig.HOME, key = "'home'")
  public HomeVO home() {
    HomeVO vo = new HomeVO();
    vo.setQuickCategories(categoryMapper.selectList(
            Wrappers.<Category>lambdaQuery().eq(Category::getLevel, 2).orderByAsc(Category::getSort))
        .stream().map(c -> {
          CategoryTreeVO t = new CategoryTreeVO();
          BeanUtil.copyProperties(c, t);
          return t;
        }).toList());
    vo.setTopRankings(buildTopRankings());
    vo.setDailyPitfalls(buildDailyPitfalls());
    return vo;
  }

  /**
   * 核心榜单：批量查品类 + 一次查品牌，内存按品类精确匹配分组取 TOP_N。
   * <p>不再按品类循环查库（消除 N+1），也不再对 mainCategoryIds 用 LIKE（避免 1 误匹配 10/11）。</p>
   *
   * @return 各品类 TOP 榜单
   */
  List<CategoryRanking> buildTopRankings() {
    Map<Long, Category> categoryMap = categoryMapper.selectBatchIds(HOME_CATEGORY_IDS).stream()
        .collect(Collectors.toMap(Category::getId, Function.identity()));
    if (categoryMap.isEmpty()) {
      return List.of();
    }
    List<Brand> brands = brandMapper.selectList(
        Wrappers.<Brand>lambdaQuery().orderByDesc(Brand::getPraiseRate));
    return HOME_CATEGORY_IDS.stream()
        .map(categoryMap::get)
        .filter(Objects::nonNull)
        .map(c -> {
          CategoryRanking cr = new CategoryRanking();
          cr.setCategoryId(c.getId());
          cr.setCategoryName(c.getName());
          cr.setTop3(toRanking(brands.stream()
              .filter(b -> belongsToCategory(b, c.getId()))
              .limit(HOME_TOP_N)
              .toList()));
          return cr;
        }).toList();
  }

  /**
   * 今日避坑精选（高危踩坑，批量补型号/品类名，避免 N+1）。
   *
   * @return 避坑卡片列表
   */
  List<HomePitfallVO> buildDailyPitfalls() {
    List<ModelPitfall> pits = pitfallMapper.selectList(Wrappers.<ModelPitfall>lambdaQuery()
        .eq(ModelPitfall::getIsHighRisk, 1)
        .orderByDesc(ModelPitfall::getCount)
        .last("LIMIT " + HOME_PITFALL_N));
    List<Long> modelIds = pits.stream().map(ModelPitfall::getModelId).distinct().toList();
    Map<Long, Model> modelMap = modelIds.isEmpty() ? Map.of()
        : modelMapper.selectBatchIds(modelIds).stream()
        .collect(Collectors.toMap(Model::getId, Function.identity()));
    List<Long> catIds = modelMap.values().stream().map(Model::getCategoryId).distinct().toList();
    Map<Long, Category> catMap = catIds.isEmpty() ? Map.of()
        : categoryMapper.selectBatchIds(catIds).stream()
        .collect(Collectors.toMap(Category::getId, Function.identity()));
    return pits.stream().map(p -> {
      HomePitfallVO hp = new HomePitfallVO();
      hp.setModelId(p.getModelId());
      hp.setDescription(p.getDescription());
      hp.setCount(p.getCount());
      Model m = modelMap.get(p.getModelId());
      if (m != null) {
        hp.setModelName(m.getName());
        Category c = catMap.get(m.getCategoryId());
        if (c != null) {
          hp.setCategoryName(c.getName());
        }
      }
      return hp;
    }).toList();
  }

  /**
   * 品牌列表转排行榜 VO（rank 从 1 开始递增）。
   *
   * @param brands 已排序的品牌列表
   * @return 带 rank 的排行榜
   */
  List<BrandRankingVO> toRanking(List<Brand> brands) {
    return IntStream.range(0, brands.size())
        .mapToObj(i -> {
          Brand b = brands.get(i);
          BrandRankingVO r = new BrandRankingVO();
          BeanUtil.copyProperties(b, r);
          r.setBrandId(b.getId());
          r.setRank(i + 1);
          return r;
        }).toList();
  }

  /**
   * 判断品牌主营品类是否包含指定品类ID（精确匹配，替代 LIKE 避免 1 误匹配 10/11）。
   *
   * @param brand 品牌
   * @param cid   品类ID
   * @return 是否主营该品类
   */
  boolean belongsToCategory(Brand brand, Long cid) {
    String ids = brand.getMainCategoryIds();
    if (ids == null || ids.isBlank()) {
      return false;
    }
    String key = String.valueOf(cid);
    return Arrays.stream(ids.split(",")).map(String::trim).anyMatch(key::equals);
  }
}
