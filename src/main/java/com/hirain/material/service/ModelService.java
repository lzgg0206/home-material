package com.hirain.material.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.common.BizException;
import com.hirain.material.entity.Brand;
import com.hirain.material.entity.Model;
import com.hirain.material.entity.ModelKeyword;
import com.hirain.material.entity.ModelPitfall;
import com.hirain.material.entity.ModelReputation;
import com.hirain.material.enums.PitfallTypeEnum;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.ModelKeywordMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.ModelPitfallMapper;
import com.hirain.material.mapper.ModelReputationMapper;
import com.hirain.material.vo.KeywordVO;
import com.hirain.material.vo.ModelDetailVO;
import com.hirain.material.vo.ModelSimpleVO;
import com.hirain.material.vo.PitfallGroupVO;
import com.hirain.material.vo.PitfallVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 型号业务：详情聚合（核心页面）。
 */
@Service
public class ModelService {

  /** 相关推荐：同价位区间下限倍数。 */
  private static final BigDecimal PRICE_RANGE_LOWER = new BigDecimal("0.7");

  /** 相关推荐：同价位区间上限倍数。 */
  private static final BigDecimal PRICE_RANGE_UPPER = new BigDecimal("1.3");

  /** 相关推荐：同价位竞品最大条数。 */
  private static final int RELATED_LIMIT = 5;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private BrandMapper brandMapper;

  @Autowired
  private ModelReputationMapper reputationMapper;

  @Autowired
  private ModelKeywordMapper keywordMapper;

  @Autowired
  private ModelPitfallMapper pitfallMapper;

  /**
   * 型号详情聚合：基础信息 + 口碑总览 + 关键词云 + 优点 + 踩坑(分类+高危置顶) + 相关推荐。
   *
   * @param id 型号ID
   * @return 型号详情
   */
  public ModelDetailVO detail(Long id) {
    Model model = modelMapper.selectById(id);
    if (model == null) {
      throw new BizException("型号不存在");
    }
    Brand brand = brandMapper.selectById(model.getBrandId());
    ModelReputation rep = reputationMapper.selectOne(
        Wrappers.<ModelReputation>lambdaQuery().eq(ModelReputation::getModelId, id));

    ModelDetailVO vo = new ModelDetailVO();
    vo.setModelId(model.getId());
    vo.setModelName(model.getName());
    vo.setSpec(model.getSpec());
    vo.setPrice(model.getPrice());
    vo.setEcoLevel(model.getEcoLevel());
    vo.setSellingPoints(splitTags(model.getSellingPoints()));
    if (brand != null) {
      vo.setBrandId(brand.getId());
      vo.setBrandName(brand.getName());
      vo.setBrandLogo(brand.getLogo());
    }
    if (rep != null) {
      vo.setPraiseRate(rep.getPraiseRate());
      vo.setPitfallRate(rep.getPitfallRate());
      vo.setSampleCount(rep.getSampleCount());
    }

    // 关键词云（按提及数降序）
    List<KeywordVO> keywordVOS = keywordMapper.selectList(
        Wrappers.<ModelKeyword>lambdaQuery()
            .eq(ModelKeyword::getModelId, id)
            .orderByDesc(ModelKeyword::getMentionCount))
        .stream().map(k -> {
          KeywordVO kv = new KeywordVO();
          BeanUtil.copyProperties(k, kv);
          return kv;
        }).toList();
    vo.setKeywords(keywordVOS);
    vo.setPros(keywordVOS.stream()
        .filter(k -> k.getSentiment() != null && k.getSentiment() == 1).toList());

    // 踩坑（高危置顶 → 频次降序 → 按类型分组）
    List<ModelPitfall> pitfalls = pitfallMapper.selectList(
        Wrappers.<ModelPitfall>lambdaQuery()
            .eq(ModelPitfall::getModelId, id)
            .orderByDesc(ModelPitfall::getIsHighRisk)
            .orderByDesc(ModelPitfall::getCount));
    vo.setPitfalls(groupPitfalls(pitfalls));

    // 相关推荐：同品牌其他型号 + 同品类同价位竞品
    vo.setRelatedSameBrand(toSimpleList(modelMapper.selectList(
        Wrappers.<Model>lambdaQuery()
            .eq(Model::getBrandId, model.getBrandId()).ne(Model::getId, id))));
    BigDecimal price = model.getPrice() == null ? BigDecimal.ZERO : model.getPrice();
    vo.setRelatedSamePrice(toSimpleList(modelMapper.selectList(
        Wrappers.<Model>lambdaQuery()
            .eq(Model::getCategoryId, model.getCategoryId())
            .ne(Model::getId, id)
            .between(Model::getPrice, price.multiply(PRICE_RANGE_LOWER),
                price.multiply(PRICE_RANGE_UPPER))
            .last("LIMIT " + RELATED_LIMIT))));
    return vo;
  }

  /** 踩坑按类型分组（保持查询时的高危置顶顺序） */
  List<PitfallGroupVO> groupPitfalls(List<ModelPitfall> pitfalls) {
    Map<String, List<ModelPitfall>> grouped = pitfalls.stream()
        .collect(Collectors.groupingBy(
            p -> p.getType() == null ? "other" : p.getType(),
            LinkedHashMap::new, Collectors.toList()));
    return grouped.entrySet().stream().map(e -> {
      PitfallGroupVO g = new PitfallGroupVO();
      g.setType(e.getKey());
      g.setTypeName(typeName(e.getKey()));
      g.setItems(e.getValue().stream().map(p -> {
        PitfallVO pv = new PitfallVO();
        BeanUtil.copyProperties(p, pv);
        pv.setTypeName(typeName(p.getType()));
        return pv;
      }).toList());
      return g;
    }).toList();
  }

  /** 型号列表转简要 VO（批量补口碑） */
  private List<ModelSimpleVO> toSimpleList(List<Model> models) {
    if (models.isEmpty()) {
      return List.of();
    }
    Map<Long, ModelReputation> repMap = reputationMapper.selectList(
        Wrappers.<ModelReputation>lambdaQuery()
            .in(ModelReputation::getModelId, models.stream().map(Model::getId).toList()))
        .stream().collect(Collectors.toMap(ModelReputation::getModelId, Function.identity()));
    return models.stream().map(m -> {
      ModelSimpleVO s = new ModelSimpleVO();
      BeanUtil.copyProperties(m, s);
      ModelReputation r = repMap.get(m.getId());
      if (r != null) {
        s.setPraiseRate(r.getPraiseRate());
        s.setPitfallRate(r.getPitfallRate());
      }
      return s;
    }).toList();
  }

  List<String> splitTags(String s) {
    if (StrUtil.isBlank(s)) {
      return List.of();
    }
    return Arrays.stream(s.split(",")).map(String::trim).filter(StrUtil::isNotBlank).toList();
  }

  String typeName(String type) {
    return PitfallTypeEnum.fromCode(type).getDescription();
  }
}
