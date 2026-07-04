package com.hirain.material.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.entity.Category;
import com.hirain.material.mapper.CategoryMapper;
import com.hirain.material.vo.CategoryTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 品类业务。
 */
@Service
public class CategoryService {

  /** 根父ID：parent_id 为此值（或 null）的视为一级品类。 */
  private static final Long ROOT_PARENT_ID = 0L;

  @Autowired
  private CategoryMapper categoryMapper;

  /**
   * 查询完整品类树（按 sort 排序，一次查库 + 内存分组建树）。
   *
   * @return 一级品类树
   */
  public List<CategoryTreeVO> getTree() {
    List<Category> all = categoryMapper.selectList(
        Wrappers.<Category>lambdaQuery().orderByAsc(Category::getSort));
    Map<Long, List<Category>> byParent = all.stream()
        .collect(Collectors.groupingBy(c -> c.getParentId() == null ? ROOT_PARENT_ID : c.getParentId()));
    return byParent.getOrDefault(ROOT_PARENT_ID, List.of()).stream()
        .map(c -> toNode(c, byParent))
        .toList();
  }

  private CategoryTreeVO toNode(Category c, Map<Long, List<Category>> byParent) {
    CategoryTreeVO vo = new CategoryTreeVO();
    BeanUtil.copyProperties(c, vo);
    List<CategoryTreeVO> children = byParent.getOrDefault(c.getId(), List.of()).stream()
        .map(x -> toNode(x, byParent))
        .toList();
    vo.setChildren(children);
    return vo;
  }
}
