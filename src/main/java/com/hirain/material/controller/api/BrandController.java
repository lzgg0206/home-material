package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.dto.BrandRankingQuery;
import com.hirain.material.service.BrandService;
import com.hirain.material.vo.BrandRankingVO;
import com.hirain.material.vo.ModelSimpleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 品牌接口（C 端）。
 */
@Tag(name = "品牌", description = "品牌排行榜与品牌下型号")
@RestController
@RequestMapping("/api/brand")
public class BrandController {

  @Autowired
  private BrandService brandService;

  /**
   * 品牌排行榜（多维度 + 筛选 + 分页）。
   *
   * @param query 查询参数
   * @return 排行榜分页
   */
  @Operation(summary = "品牌排行榜")
  @GetMapping("/ranking")
  public Result<Page<BrandRankingVO>> ranking(BrandRankingQuery query) {
    return Result.success(brandService.ranking(query));
  }

  /**
   * 品牌下型号列表。
   *
   * @param id 品牌ID
   * @return 型号简要列表
   */
  @Operation(summary = "品牌下型号列表")
  @GetMapping("/{id}/models")
  public Result<List<ModelSimpleVO>> models(@PathVariable Long id) {
    return Result.success(brandService.modelsByBrand(id));
  }
}
