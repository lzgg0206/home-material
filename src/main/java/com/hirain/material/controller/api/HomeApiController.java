package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.service.HomeService;
import com.hirain.material.vo.HomeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 首页与健康检查接口（C 端）。
 */
@Tag(name = "首页", description = "首页聚合与健康检查")
@RestController
@RequestMapping("/api/home")
public class HomeApiController {

  @Autowired
  private HomeService homeService;

  /**
   * 健康检查，用于部署探活与联调验证。
   *
   * @return 服务状态信息
   */
  @Operation(summary = "健康检查")
  @GetMapping("/health")
  public Result<Map<String, Object>> health() {
    return Result.success(Map.of(
        "status", "UP",
        "service", "home-material",
        "version", "1.0.0",
        "time", LocalDateTime.now().toString()
    ));
  }

  /**
   * 首页聚合（快捷品类 + 核心榜单 TOP3 + 今日避坑精选）。
   *
   * @return 首页数据
   */
  @Operation(summary = "首页聚合")
  @GetMapping
  public Result<HomeVO> home() {
    return Result.success(homeService.home());
  }
}
