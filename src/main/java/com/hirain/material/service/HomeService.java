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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页聚合业务：快捷品类 + 核心榜单 + 避坑精选。
 */
@Service
public class HomeService {

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
  public HomeVO home() {
    HomeVO vo = new HomeVO();
    // 快捷品类（高频二级品类）
    vo.setQuickCategories(categoryMapper.selectList(
            Wrappers.<Category>lambdaQuery().eq(Category::getLevel, 2).orderByAsc(Category::getSort))
        .stream().map(c -> {
          CategoryTreeVO t = new CategoryTreeVO();
          BeanUtil.copyProperties(c, t);
          return t;
        }).toList());

    // 核心榜单：硬装主材(1) / 厨卫(3) / 软装(5) 各 TOP3
    List<CategoryRanking> rankings = new ArrayList<>();
    for (Long cid : List.of(1L, 3L, 5L)) {
      Category cat = categoryMapper.selectById(cid);
      if (cat == null) {
        continue;
      }
      List<Brand> top3 = brandMapper.selectList(Wrappers.<Brand>lambdaQuery()
          .like(Brand::getMainCategoryIds, String.valueOf(cid))
          .orderByDesc(Brand::getPraiseRate).last("LIMIT 3"));
      CategoryRanking cr = new CategoryRanking();
      cr.setCategoryId(cat.getId());
      cr.setCategoryName(cat.getName());
      cr.setTop3(toRanking(top3));
      rankings.add(cr);
    }
    vo.setTopRankings(rankings);

    // 今日避坑精选（高危踩坑，批量补型号/品类名，避免 N+1）
    List<ModelPitfall> pits = pitfallMapper.selectList(Wrappers.<ModelPitfall>lambdaQuery()
        .eq(ModelPitfall::getIsHighRisk, 1).orderByDesc(ModelPitfall::getCount).last("LIMIT 3"));
    List<Long> modelIds = pits.stream().map(ModelPitfall::getModelId).distinct().toList();
    Map<Long, Model> modelMap = modelIds.isEmpty() ? Map.of()
        : modelMapper.selectBatchIds(modelIds).stream()
        .collect(Collectors.toMap(Model::getId, Function.identity()));
    List<Long> catIds = modelMap.values().stream().map(Model::getCategoryId).distinct().toList();
    Map<Long, Category> catMap = catIds.isEmpty() ? Map.of()
        : categoryMapper.selectBatchIds(catIds).stream()
        .collect(Collectors.toMap(Category::getId, Function.identity()));
    vo.setDailyPitfalls(pits.stream().map(p -> {
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
    }).toList());
    return vo;
  }

  private List<BrandRankingVO> toRanking(List<Brand> brands) {
    List<BrandRankingVO> list = new ArrayList<>();
    int rank = 1;
    for (Brand b : brands) {
      BrandRankingVO r = new BrandRankingVO();
      BeanUtil.copyProperties(b, r);
      r.setBrandId(b.getId());
      r.setRank(rank++);
      list.add(r);
    }
    return list;
  }
}
