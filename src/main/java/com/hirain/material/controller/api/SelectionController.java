package com.hirain.material.controller.api;

import com.hirain.material.common.Result;
import com.hirain.material.entity.SelectionItem;
import com.hirain.material.entity.SelectionList;
import com.hirain.material.service.SelectionService;
import com.hirain.material.vo.BudgetVO;
import com.hirain.material.vo.SelectionItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 自选清单接口（C 端，需登录）。
 * 提供清单及清单项的增删改查、预算统计与 Excel 导出，路径前缀 /api/list。
 *
 * @author lingzhi.Wang
 */
@Tag(name = "自选清单", description = "清单与清单项 CRUD + 预算 + 导出")
@RestController
@RequestMapping("/api/list")
public class SelectionController {

  @Autowired
  private SelectionService selectionService;

  /**
   * 查询当前用户的全部自选清单。
   *
   * @return 当前用户的清单列表
   */
  @Operation(summary = "我的所有清单")
  @GetMapping
  public Result<List<SelectionList>> myLists() {
    return Result.success(selectionService.myLists());
  }

  /**
   * 新建自选清单。
   *
   * @param list 清单基本信息
   * @return 新建后的清单
   */
  @Operation(summary = "新建清单")
  @PostMapping
  public Result<SelectionList> createList(@RequestBody SelectionList list) {
    return Result.success(selectionService.createList(list));
  }

  /**
   * 查询指定清单下的所有清单项。
   *
   * @param id 清单ID
   * @return 清单项列表
   */
  @Operation(summary = "清单下所有项")
  @GetMapping("/{id}/items")
  public Result<List<SelectionItemVO>> items(@PathVariable Long id) {
    return Result.success(selectionService.items(id));
  }

  /**
   * 向指定清单新增清单项。
   *
   * @param id 清单ID
   * @param item 清单项内容
   * @return 新增后的清单项
   */
  @Operation(summary = "新增清单项")
  @PostMapping("/{id}/item")
  public Result<SelectionItem> addItem(@PathVariable Long id, @RequestBody SelectionItem item) {
    return Result.success(selectionService.addItem(id, item));
  }

  /**
   * 修改指定清单项。
   *
   * @param itemId 清单项ID
   * @param item 清单项内容
   * @return 修改后的清单项
   */
  @Operation(summary = "修改清单项")
  @PutMapping("/item/{itemId}")
  public Result<SelectionItem> updateItem(@PathVariable Long itemId, @RequestBody SelectionItem item) {
    return Result.success(selectionService.updateItem(itemId, item));
  }

  /**
   * 删除指定清单项。
   *
   * @param itemId 清单项ID
   * @return 空结果
   */
  @Operation(summary = "删除清单项")
  @DeleteMapping("/item/{itemId}")
  public Result<Void> deleteItem(@PathVariable Long itemId) {
    selectionService.deleteItem(itemId);
    return Result.success();
  }

  /**
   * 统计指定清单的预算信息。
   *
   * @param id 清单ID
   * @return 预算统计结果
   */
  @Operation(summary = "预算统计")
  @GetMapping("/{id}/budget")
  public Result<BudgetVO> budget(@PathVariable Long id) {
    return Result.success(selectionService.budget(id));
  }

  /**
   * 导出指定清单为 Excel 文件并写入响应流。
   *
   * @param id 清单ID
   * @param response HTTP 响应，用于写出文件流
   */
  @Operation(summary = "导出清单 Excel")
  @GetMapping("/{id}/export")
  public void export(@PathVariable Long id, HttpServletResponse response) {
    selectionService.export(id, response);
  }
}
