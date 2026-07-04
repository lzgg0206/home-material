package com.hirain.material.controller.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Category;
import com.hirain.material.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * B 端 - 品类管理。
 */
@Tag(name = "B端-品类")
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController extends BaseAdminController<Category> {

  @Autowired
  private CategoryMapper categoryMapper;

  @Override
  protected BaseMapper<Category> mapper() {
    return categoryMapper;
  }
}
