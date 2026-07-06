package com.hirain.material.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.config.AiBrandSchema;
import com.hirain.material.config.AiProperties;
import com.hirain.material.config.CacheConfig;
import com.hirain.material.entity.BrandAiRanking;
import com.hirain.material.mapper.BrandAiRankingMapper;
import com.hirain.material.vo.BrandRankingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * AI 品牌榜单：调大模型生成 + 落库 + 读取。
 *
 * <p>按 {@code (city, categoryId, dimension)} 三元组异步预热入库，前端读库读缓存。
 * 大模型走 {@link AiBrandSchema#BRAND_RANKING} 强约束输出，固定结构；慢调用隔离在后台，
 * 失败不影响 C 端——{@code BrandService} 有静态数据兜底。</p>
 *
 * @author lingzhi.Wang
 */
@Slf4j
@Service
public class AiBrandService {

  @Autowired
  private LlmClient llmClient;

  @Autowired
  private BrandAiRankingMapper aiRankingMapper;

  @Autowired
  private AiProperties props;

  /**
   * 读取 AI 榜单（{@code @Cacheable}，缓存 miss 查库）。
   *
   * @param city       城市
   * @param categoryId 品类ID
   * @param dimension  维度
   * @return 品牌列表，无数据返回空列表
   */
  @Cacheable(value = CacheConfig.AI_RANKING,
      key = "#city + '_' + #categoryId + '_' + #dimension", unless = "#result.isEmpty()")
  public List<BrandRankingVO> getRanking(String city, Long categoryId, String dimension) {
    BrandAiRanking row = findRow(city, categoryId, dimension);
    if (row == null) {
      return List.of();
    }
    return parseBrands(row.getBrandsJson());
  }

  /**
   * 异步预热：调大模型生成并落库（供 BrandService miss 时触发）。
   *
   * @param city       城市
   * @param categoryId 品类ID
   * @param dimension  维度
   */
  @Async
  public void syncAsync(String city, Long categoryId, String dimension) {
    sync(city, categoryId, dimension);
  }

  /**
   * 同步预热：调大模型生成并落库（阻塞，定时任务用）。任何异常只记日志，不外抛。
   *
   * @param city       城市
   * @param categoryId 品类ID
   * @param dimension  维度
   */
  public void sync(String city, Long categoryId, String dimension) {
    try {
      String content = llmClient.chatJson(systemPrompt(),
          buildPrompt(city, categoryId, dimension), AiBrandSchema.BRAND_RANKING);
      String clean = stripJson(content);
      List<BrandRankingVO> brands = parseBrands(clean);
      if (brands.isEmpty()) {
        log.warn("[AI榜单] 解析为空 city={} cat={} dim={}", city, categoryId, dimension);
        return;
      }
      upsert(city, categoryId, dimension, clean);
      log.info("[AI榜单] 预热完成 city={} cat={} dim={} size={}", city, categoryId, dimension, brands.size());
    } catch (Exception e) {
      log.error("[AI榜单] 预热失败 city={} cat={} dim={}", city, categoryId, dimension, e);
    }
  }

  /** upsert：存在则更新 brands_json，不存在则插入。 */
  private void upsert(String city, Long categoryId, String dimension, String brandsJson) {
    BrandAiRanking row = findRow(city, categoryId, dimension);
    if (row == null) {
      row = new BrandAiRanking();
      row.setCity(city);
      row.setCategoryId(categoryId);
      row.setDimension(dimension);
      row.setBrandsJson(brandsJson);
      row.setModel(props.getModel());
      aiRankingMapper.insert(row);
    } else {
      row.setBrandsJson(brandsJson);
      row.setModel(props.getModel());
      aiRankingMapper.updateById(row);
    }
  }

  private BrandAiRanking findRow(String city, Long categoryId, String dimension) {
    return aiRankingMapper.selectOne(Wrappers.<BrandAiRanking>lambdaQuery()
        .eq(BrandAiRanking::getCity, city)
        .eq(BrandAiRanking::getCategoryId, categoryId)
        .eq(BrandAiRanking::getDimension, dimension));
  }

  private String systemPrompt() {
    return "你是家装建材行业的小红书口碑分析师。根据用户给定的城市与品类，"
        + "结合小红书最新真实用户测评与笔记，输出该城市该品类下最受认可的品牌口碑榜单。"
        + "严格按给定 JSON Schema 输出，brands 数组内每个品牌字段："
        + "name(品牌名), origin(domestic/imported), tier(high/mid/entry), "
        + "praiseRate(0-100数字), pitfallCount(整数), priceMin(数字), priceMax(数字), "
        + "tags(2-4个中文标签数组), highlight(一句小红书口碑亮点)。"
        + "按所给维度排序，给出 10-15 个品牌，不要输出 JSON 以外的任何文字。";
  }

  private String buildPrompt(String city, Long categoryId, String dimension) {
    String dimDesc = switch (dimension) {
      case "cost" -> "性价比（好评率高且均价低优先）";
      case "highend" -> "高端品质（仅 high 定位）";
      case "lowpitfall" -> "低踩坑率（踩坑反馈少优先）";
      case "eco" -> "环保等级（环保口碑优先）";
      default -> "综合口碑（好评率优先）";
    };
    return StrUtil.format("城市：{}\n品类ID：{}\n榜单维度：{}\n请生成该城市该品类的品牌口碑榜单 JSON。",
        city, categoryId, dimDesc);
  }

  /** 容错：去掉 markdown 代码块包裹与多余空白。 */
  private String stripJson(String content) {
    if (StrUtil.isBlank(content)) {
      return "{\"brands\":[]}";
    }
    String s = content.trim();
    if (s.startsWith("```")) {
      s = s.replaceAll("^```(json)?", "").replaceAll("```$", "").trim();
    }
    return s;
  }

  /** 解析 {brands:[...]} JSON 为 VO 列表，解析失败返回空列表。 */
  private List<BrandRankingVO> parseBrands(String json) {
    List<BrandRankingVO> list = new ArrayList<>();
    try {
      JSONArray arr = JSONUtil.parseObj(json).getJSONArray("brands");
      if (arr == null) {
        return list;
      }
      for (Object o : arr) {
        JSONObject b = (JSONObject) o;
        BrandRankingVO vo = new BrandRankingVO();
        vo.setName(b.getStr("name"));
        vo.setOrigin(b.getStr("origin"));
        vo.setTier(b.getStr("tier"));
        vo.setPraiseRate(toBd(b.get("praiseRate")));
        vo.setPitfallCount(b.getInt("pitfallCount"));
        vo.setPriceMin(toBd(b.get("priceMin")));
        vo.setPriceMax(toBd(b.get("priceMax")));
        vo.setHighlight(b.getStr("highlight"));
        JSONArray tags = b.getJSONArray("tags");
        if (tags != null) {
          vo.setTags(tags.toList(String.class));
        }
        list.add(vo);
      }
    } catch (Exception e) {
      log.warn("[AI榜单] 解析 JSON 失败: {}", e.getMessage());
    }
    return list;
  }

  private BigDecimal toBd(Object v) {
    if (v == null) {
      return null;
    }
    try {
      return new BigDecimal(v.toString());
    } catch (Exception e) {
      return null;
    }
  }
}
