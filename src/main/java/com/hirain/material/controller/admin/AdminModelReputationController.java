package com.hirain.material.controller.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.ModelReputation;
import com.hirain.material.mapper.ModelReputationMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * B 端 - 型号口碑聚合管理。
 * 继承通用 CRUD 基类，路径前缀 /admin/reputation。
 *
 * @author lingzhi.Wang
 */
@Tag(name = "B端-口碑聚合")
@RestController
@RequestMapping("/admin/reputation")
public class AdminModelReputationController extends BaseAdminController<ModelReputation> {

  @Autowired
  private ModelReputationMapper modelReputationMapper;

  @Override
  protected BaseMapper<ModelReputation> mapper() {
    return modelReputationMapper;
  }
}
