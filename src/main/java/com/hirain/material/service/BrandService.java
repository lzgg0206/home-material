package com.hirain.material.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hirain.material.config.AiProperties;
import com.hirain.material.config.CacheConfig;
import com.hirain.material.dto.BrandRankingQuery;
import com.hirain.material.entity.Brand;
import com.hirain.material.entity.Model;
import com.hirain.material.entity.ModelReputation;
import com.hirain.material.entity.UserProfile;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.ModelReputationMapper;
import com.hirain.material.vo.BrandRankingVO;
import com.hirain.material.vo.ModelSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 品牌业务：多维度排行榜 + 品牌下型号。
 *
 * <p>排行榜优先取 AI 大模型生成的地域榜单（按 当前登录用户城市 匹配），命中则在内存做
 * 产地/价格档过滤、维度排序与分页；未命中走静态品牌库兜底，并异步触发 AI 预热供下次使用。</p>
 *
 * @author lingzhi.Wang
 */
@Service
public class BrandService {

  /** 经济档价格上限（含）。 */
  private static final BigDecimal PRICE_ECONOMIC_MAX = new BigDecimal("1000");

  /** 品质档价格上限（含）。 */
  private static final BigDecimal PRICE_QUALITY_MAX = new BigDecimal("5000");

  @Autowired
  private BrandMapper brandMapper;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private ModelReputationMapper reputationMapper;

  @Autowired
  private AiBrandService aiBrandService;

  @Autowired
  private ProfileService profileService;

  @Autowired
  private AiProperties aiProperties;

  /**
   * 品牌排行榜（AI 地域榜单优先，静态库兜底；按品类/维度/产地/价格档筛选 + 分页）。
   *
   * <p>缓存 key 含城市维度，未登录或档案无城市时统一兜底为默认城市。</p>
   *
   * @param q 查询参数
   * @return 排行榜分页
   */
  @Cacheable(value = CacheConfig.BRAND_RANKING, sync = true,
      key = "#root.target.currentCity() + '_' + #q.categoryId + '_' + #q.dimension + '_' + #q.origin + '_' + #q.priceRange + '_' + #q.page + '_' + #q.size")
  public Page<BrandRankingVO> ranking(BrandRankingQuery q) {
    String city = currentCity();
    String dim = StrUtil.isBlank(q.getDimension()) ? "overall" : q.getDimension();

    // 1. 优先 AI 地域榜单：命中则在内存过滤/排序/分页
    List<BrandRankingVO> ai = aiBrandService.getRanking(city, q.getCategoryId(), dim);
    if (ai != null && !ai.isEmpty()) {
      return paginate(filterAndSort(ai, q), q);
    }
    // 2. miss：异步触发 AI 预热，本次走静态库兜底
    aiBrandService.syncAsync(city, q.getCategoryId(), dim);
    return staticRanking(q);
  }

  /**
   * 当前登录用户城市（缓存 key 与业务共用）。未登录或档案无城市兜底默认城市。
   *
   * @return 城市名
   */
  public String currentCity() {
    try {
      UserProfile p = profileService.myProfile();
      String c = p == null ? null : p.getCity();
      return StrUtil.isBlank(c) ? aiProperties.getDefaultCity() : c;
    } catch (Exception e) {
      return aiProperties.getDefaultCity();
    }
  }

  /**
   * 静态品牌库排行榜（原有逻辑，作为 AI 数据缺失时的兜底）。
   *
   * @param q 查询参数
   * @return 排行榜分页
   */
  private Page<BrandRankingVO> staticRanking(BrandRankingQuery q) {
    Page<Brand> page = new Page<>(q.getPage(), q.getSize());
    String cid = q.getCategoryId() == null ? null : String.valueOf(q.getCategoryId());
    String dim = StrUtil.isBlank(q.getDimension()) ? "overall" : q.getDimension();

    LambdaQueryWrapper<Brand> w = Wrappers.<Brand>lambdaQuery()
        .eq(StrUtil.isNotBlank(q.getOrigin()), Brand::getOrigin, q.getOrigin());
    // 用 FIND_IN_SET 精确匹配逗号分隔的品类ID，避免 LIKE 把 cid=1 误匹配到 10/11
    if (StrUtil.isNotBlank(cid)) {
      w.apply("FIND_IN_SET({0}, main_category_ids)", cid);
    }
    if ("highend".equals(dim)) {
      w.eq(Brand::getTier, "high");
    }
    applyPriceRange(w, q.getPriceRange());
    applyOrder(w, dim);
    brandMapper.selectPage(page, w);

    Page<BrandRankingVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
    long start = (page.getCurrent() - 1) * page.getSize();
    List<BrandRankingVO> records = new ArrayList<>();
    for (Brand b : page.getRecords()) {
      BrandRankingVO vo = new BrandRankingVO();
      BeanUtil.copyProperties(b, vo);
      vo.setBrandId(b.getId());
      vo.setRank((int) (++start));
      vo.setTags(buildTags(b));
      records.add(vo);
    }
    result.setRecords(records);
    return result;
  }

  /**
   * 品牌下型号列表（含口碑概览）。
   *
   * @param brandId 品牌ID
   * @return 型号简要列表
   */
  public List<ModelSimpleVO> modelsByBrand(Long brandId) {
    List<Model> models = modelMapper.selectList(
        Wrappers.<Model>lambdaQuery().eq(Model::getBrandId, brandId));
    if (models.isEmpty()) {
      return List.of();
    }
    Map<Long, ModelReputation> repMap = reputationMapper.selectList(
            Wrappers.<ModelReputation>lambdaQuery()
                .in(ModelReputation::getModelId, models.stream().map(Model::getId).toList()))
        .stream().collect(Collectors.toMap(ModelReputation::getModelId, Function.identity()));
    return models.stream().map(m -> {
      ModelSimpleVO vo = new ModelSimpleVO();
      BeanUtil.copyProperties(m, vo);
      ModelReputation r = repMap.get(m.getId());
      if (r != null) {
        vo.setPraiseRate(r.getPraiseRate());
        vo.setPitfallRate(r.getPitfallRate());
      }
      return vo;
    }).toList();
  }

  /** AI 榜单内存过滤：产地 + 价格档。 */
  private List<BrandRankingVO> filterAndSort(List<BrandRankingVO> src, BrandRankingQuery q) {
    return src.stream()
        .filter(b -> StrUtil.isBlank(q.getOrigin()) || q.getOrigin().equals(b.getOrigin()))
        .filter(b -> matchPriceRange(b.getPriceMin(), q.getPriceRange()))
        .sorted(comparator(StrUtil.isBlank(q.getDimension()) ? "overall" : q.getDimension()))
        .toList();
  }

  private boolean matchPriceRange(BigDecimal priceMin, String range) {
    if (StrUtil.isBlank(range) || priceMin == null) {
      return true;
    }
    return switch (range) {
      case "economic" -> priceMin.compareTo(PRICE_ECONOMIC_MAX) < 0;
      case "quality" -> priceMin.compareTo(PRICE_ECONOMIC_MAX) >= 0
          && priceMin.compareTo(PRICE_QUALITY_MAX) < 0;
      case "high" -> priceMin.compareTo(PRICE_QUALITY_MAX) >= 0;
      default -> true;
    };
  }

  /** AI 榜单排序：按维度，null 一律排末尾。 */
  private Comparator<BrandRankingVO> comparator(String dim) {
    Comparator<BrandRankingVO> byPraise = Comparator.comparing(BrandRankingVO::getPraiseRate,
        Comparator.nullsLast(Comparator.reverseOrder()));
    if ("lowpitfall".equals(dim)) {
      return Comparator.comparing(BrandRankingVO::getPitfallCount,
          Comparator.nullsLast(Comparator.naturalOrder()));
    }
    if ("cost".equals(dim)) {
      return byPraise.thenComparing(BrandRankingVO::getPriceMin,
          Comparator.nullsLast(Comparator.naturalOrder()));
    }
    return byPraise;
  }

  /** AI 榜单内存分页 + 重算 rank/brandId（AI 品牌无库内ID，用负序号占位保证前端 key 唯一）。 */
  private Page<BrandRankingVO> paginate(List<BrandRankingVO> sorted, BrandRankingQuery q) {
    int total = sorted.size();
    int from = Math.min(Math.max((q.getPage() - 1) * q.getSize(), 0), total);
    int to = Math.min(from + q.getSize(), total);
    List<BrandRankingVO> records = new ArrayList<>(sorted.subList(from, to));
    long rank = from + 1;
    for (BrandRankingVO b : records) {
      b.setRank((int) rank);
      if (b.getBrandId() == null) {
        b.setBrandId(-rank);
      }
      rank++;
    }
    Page<BrandRankingVO> p = new Page<>(q.getPage(), q.getSize(), total);
    p.setRecords(records);
    return p;
  }

  private void applyPriceRange(LambdaQueryWrapper<Brand> w, String range) {
    if (StrUtil.isBlank(range)) {
      return;
    }
    switch (range) {
      case "economic" -> w.lt(Brand::getPriceMin, PRICE_ECONOMIC_MAX);
      case "quality" -> w.ge(Brand::getPriceMin, PRICE_ECONOMIC_MAX)
          .lt(Brand::getPriceMin, PRICE_QUALITY_MAX);
      case "high" -> w.ge(Brand::getPriceMin, PRICE_QUALITY_MAX);
      default -> { /* 全部 */ }
    }
  }

  private void applyOrder(LambdaQueryWrapper<Brand> w, String dim) {
    switch (dim) {
      case "lowpitfall" -> w.orderByAsc(Brand::getPitfallCount);
      case "cost" -> w.orderByDesc(Brand::getPraiseRate).orderByAsc(Brand::getPriceMin);
      default -> w.orderByDesc(Brand::getPraiseRate);
    }
  }

  /**
   * 生成品牌展示标签（档位 + 产地 + 好评率）。
   *
   * @param b 品牌
   * @return 标签列表
   */
  List<String> buildTags(Brand b) {
    List<String> tags = new ArrayList<>();
    String tier = b.getTier();
    tags.add("high".equals(tier) ? "高端品质"
        : "mid".equals(tier) ? "中端主流"
        : "entry".equals(tier) ? "入门性价比" : "综合");
    tags.add("domestic".equals(b.getOrigin()) ? "国产" : "进口");
    if (b.getPraiseRate() != null) {
      tags.add("好评" + b.getPraiseRate().intValue() + "%");
    }
    return tags;
  }
}
