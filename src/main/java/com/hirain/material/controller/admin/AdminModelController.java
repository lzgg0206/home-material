package com.hirain.material.controller.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Model;
import com.hirain.material.mapper.ModelMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * B 端 - 型号管理。
 */
@Tag(name = "B端-型号")
@RestController
@RequestMapping("/admin/model")
public class AdminModelController extends BaseAdminController<Model> {

  @Autowired
  private ModelMapper modelMapper;

  @Override
  protected BaseMapper<Model> mapper() {
    return modelMapper;
  }
}
