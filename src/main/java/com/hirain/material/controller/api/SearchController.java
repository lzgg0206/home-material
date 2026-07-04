package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.service.SearchService;
import com.hirain.material.vo.SearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 搜索接口（C 端）。
 */
@Tag(name = "搜索", description = "全局搜索与热搜")
@RestController
@RequestMapping("/api/search")
public class SearchController {

  @Autowired
  private SearchService searchService;

  /**
   * 全局搜索（按品类/品牌/型号分类聚合）。
   *
   * @param keyword 关键词
   * @return 分类搜索结果
   */
  @Operation(summary = "全局搜索")
  @GetMapping
  public Result<SearchVO> search(@RequestParam String keyword) {
    return Result.success(searchService.search(keyword));
  }

  /**
   * 热门搜索词。
   *
   * @return 热搜词列表
   */
  @Operation(summary = "热门搜索词")
  @GetMapping("/hot")
  public Result<List<String>> hot() {
    return Result.success(searchService.hot());
  }
}
