package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.service.ModelService;
import com.hirain.material.vo.ModelDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 型号接口（C 端）。
 */
@Tag(name = "型号", description = "型号详情聚合")
@RestController
@RequestMapping("/api/model")
public class ModelController {

  @Autowired
  private ModelService modelService;

  /**
   * 型号详情聚合（基础+口碑+关键词+优点+踩坑+相关推荐）。
   *
   * @param id 型号ID
   * @return 型号详情
   */
  @Operation(summary = "型号详情聚合")
  @GetMapping("/{id}/detail")
  public Result<ModelDetailVO> detail(@PathVariable Long id) {
    return Result.success(modelService.detail(id));
  }
}
