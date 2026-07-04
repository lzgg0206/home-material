package com.hirain.material.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hirain.material.dto.BrandRankingQuery;
import com.hirain.material.entity.Brand;
import com.hirain.material.entity.Model;
import com.hirain.material.entity.ModelReputation;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.ModelReputationMapper;
import com.hirain.material.vo.BrandRankingVO;
import com.hirain.material.vo.ModelSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 品牌业务：多维度排行榜 + 品牌下型号。
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

  /**
   * 品牌排行榜（按品类/维度/产地/价格档筛选 + 分页，rank 为全局名次）。
   *
   * @param q 查询参数
   * @return 排行榜分页
   */
  public Page<BrandRankingVO> ranking(BrandRankingQuery q) {
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
