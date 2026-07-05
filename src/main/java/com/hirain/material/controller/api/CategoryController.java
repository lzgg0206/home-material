package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.service.CategoryService;
import com.hirain.material.vo.CategoryTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品类接口（C 端）。
 * 提供品类树查询，路径前缀 /api/category。
 *
 * @author lingzhi.Wang
 */
@Tag(name = "品类", description = "品类树与分类")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  /**
   * 查询完整品类树。
   *
   * @return 一级品类树
   */
  @Operation(summary = "品类树")
  @GetMapping("/tree")
  public Result<List<CategoryTreeVO>> tree() {
    return Result.success(categoryService.getTree());
  }
}
