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
 */
@Tag(name = "自选清单", description = "清单与清单项 CRUD + 预算 + 导出")
@RestController
@RequestMapping("/api/list")
public class SelectionController {

  @Autowired
  private SelectionService selectionService;

  @Operation(summary = "我的所有清单")
  @GetMapping
  public Result<List<SelectionList>> myLists() {
    return Result.success(selectionService.myLists());
  }

  @Operation(summary = "新建清单")
  @PostMapping
  public Result<SelectionList> createList(@RequestBody SelectionList list) {
    return Result.success(selectionService.createList(list));
  }

  @Operation(summary = "清单下所有项")
  @GetMapping("/{id}/items")
  public Result<List<SelectionItemVO>> items(@PathVariable Long id) {
    return Result.success(selectionService.items(id));
  }

  @Operation(summary = "新增清单项")
  @PostMapping("/{id}/item")
  public Result<SelectionItem> addItem(@PathVariable Long id, @RequestBody SelectionItem item) {
    return Result.success(selectionService.addItem(id, item));
  }

  @Operation(summary = "修改清单项")
  @PutMapping("/item/{itemId}")
  public Result<SelectionItem> updateItem(@PathVariable Long itemId, @RequestBody SelectionItem item) {
    return Result.success(selectionService.updateItem(itemId, item));
  }

  @Operation(summary = "删除清单项")
  @DeleteMapping("/item/{itemId}")
  public Result<Void> deleteItem(@PathVariable Long itemId) {
    selectionService.deleteItem(itemId);
    return Result.success();
  }

  @Operation(summary = "预算统计")
  @GetMapping("/{id}/budget")
  public Result<BudgetVO> budget(@PathVariable Long id) {
    return Result.success(selectionService.budget(id));
  }

  @Operation(summary = "导出清单 Excel")
  @GetMapping("/{id}/export")
  public void export(@PathVariable Long id, HttpServletResponse response) {
    selectionService.export(id, response);
  }
}
