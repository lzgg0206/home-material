package com.hirain.material.controller.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Brand;
import com.hirain.material.mapper.BrandMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * B 端 - 品牌管理。
 * 继承通用 CRUD 基类，路径前缀 /admin/brand。
 *
 * @author lingzhi.Wang
 */
@Tag(name = "B端-品牌")
@RestController
@RequestMapping("/admin/brand")
public class AdminBrandController extends BaseAdminController<Brand> {

  @Autowired
  private BrandMapper brandMapper;

  @Override
  protected BaseMapper<Brand> mapper() {
    return brandMapper;
  }
}
