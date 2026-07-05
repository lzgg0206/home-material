package com.hirain.material.controller.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.ModelPitfall;
import com.hirain.material.mapper.ModelPitfallMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * B 端 - 型号踩坑点管理。
 * 继承通用 CRUD 基类，路径前缀 /admin/pitfall。
 *
 * @author lingzhi.Wang
 */
@Tag(name = "B端-踩坑点")
@RestController
@RequestMapping("/admin/pitfall")
public class AdminModelPitfallController extends BaseAdminController<ModelPitfall> {

  @Autowired
  private ModelPitfallMapper modelPitfallMapper;

  @Override
  protected BaseMapper<ModelPitfall> mapper() {
    return modelPitfallMapper;
  }
}
