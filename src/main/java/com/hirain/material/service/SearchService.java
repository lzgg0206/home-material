package com.hirain.material.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.entity.Brand;
import com.hirain.material.entity.Category;
import com.hirain.material.entity.HotKeyword;
import com.hirain.material.entity.Model;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.CategoryMapper;
import com.hirain.material.mapper.HotKeywordMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.vo.SearchItemVO;
import com.hirain.material.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 搜索业务：全局搜索 + 热搜词。
 *
 * @author lingzhi.Wang
 */
@Service
public class SearchService {

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private BrandMapper brandMapper;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private HotKeywordMapper hotKeywordMapper;

  /**
   * 全局搜索（品牌名 / 型号名 / 品类名，分类聚合返回）。
   *
   * @param keyword 关键词
   * @return 分类搜索结果
   */
  public SearchVO search(String keyword) {
    SearchVO vo = new SearchVO();
    if (StrUtil.isBlank(keyword)) {
      vo.setCategories(List.of());
      vo.setBrands(List.of());
      vo.setModels(List.of());
      return vo;
    }
    vo.setCategories(categoryMapper.selectList(Wrappers.<Category>lambdaQuery()
            .like(Category::getName, keyword).last("LIMIT 5")).stream()
        .map(c -> item(c.getId(), c.getName(), "category")).toList());
    vo.setBrands(brandMapper.selectList(Wrappers.<Brand>lambdaQuery()
            .like(Brand::getName, keyword).last("LIMIT 5")).stream()
        .map(b -> item(b.getId(), b.getName(), "brand")).toList());
    vo.setModels(modelMapper.selectList(Wrappers.<Model>lambdaQuery()
            .like(Model::getName, keyword).last("LIMIT 10")).stream()
        .map(m -> item(m.getId(), m.getName(), "model")).toList());
    return vo;
  }

  /**
   * 热门搜索词。
   *
   * @return 热搜词列表
   */
  public List<String> hot() {
    return hotKeywordMapper.selectList(Wrappers.<HotKeyword>lambdaQuery()
            .orderByDesc(HotKeyword::getSearchCount).last("LIMIT 10")).stream()
        .map(HotKeyword::getKeyword).toList();
  }

  private SearchItemVO item(Long id, String name, String type) {
    SearchItemVO it = new SearchItemVO();
    it.setId(id);
    it.setName(name);
    it.setType(type);
    return it;
  }
}
