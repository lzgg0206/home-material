package com.hirain.material.controller.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hirain.material.common.BaseEntity;
import com.hirain.material.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * B 端通用 CRUD 基类：分页 + 新增 + 修改 + 删除。
 * 子类通过实现 {@link #mapper()} 提供对应 Mapper，即自动获得通用增删改查动作，路径前缀 /admin。
 *
 * @param <T> 实体类型
 * @author lingzhi.Wang
 */
public abstract class BaseAdminController<T extends BaseEntity> {

  /** 子类提供对应 Mapper */
  protected abstract BaseMapper<T> mapper();

  /**
   * 分页查询。
   *
   * @param page 页码
   * @param size 每页条数
   * @return 分页结果
   */
  @Operation(summary = "分页查询")
  @GetMapping("/page")
  public Result<Page<T>> page(@RequestParam(defaultValue = "1") long page,
                              @RequestParam(defaultValue = "20") long size) {
    return Result.success(mapper().selectPage(new Page<>(page, size), null));
  }

  /**
   * 新增。
   *
   * @param entity 实体
   * @return 新增后的实体
   */
  @Operation(summary = "新增")
  @PostMapping
  public Result<T> add(@RequestBody T entity) {
    mapper().insert(entity);
    return Result.success(entity);
  }

  /**
   * 修改。
   *
   * @param entity 实体（需含 id）
   * @return 修改后的实体
   */
  @Operation(summary = "修改")
  @PutMapping
  public Result<T> update(@RequestBody T entity) {
    mapper().updateById(entity);
    return Result.success(entity);
  }

  /**
   * 删除（逻辑删除）。
   *
   * @param id 主键
   * @return 空
   */
  @Operation(summary = "删除")
  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable Long id) {
    mapper().deleteById(id);
    return Result.success();
  }
}
