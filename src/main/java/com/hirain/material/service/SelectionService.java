package com.hirain.material.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hirain.material.common.BizException;
import com.hirain.material.entity.Category;
import com.hirain.material.entity.Model;
import com.hirain.material.entity.SelectionItem;
import com.hirain.material.entity.SelectionList;
import com.hirain.material.entity.UserProfile;
import com.hirain.material.mapper.CategoryMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.SelectionItemMapper;
import com.hirain.material.mapper.SelectionListMapper;
import com.hirain.material.mapper.UserProfileMapper;
import com.hirain.material.vo.BudgetGroupVO;
import com.hirain.material.vo.BudgetVO;
import com.hirain.material.vo.SelectionItemVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自选清单业务：清单 + 清单项 CRUD + 预算统计 + Excel 导出。
 *
 * @author lingzhi.Wang
 */
@Slf4j
@Service
public class SelectionService {

  private static final BigDecimal HUNDRED = new BigDecimal("100");

  @Autowired
  private SelectionListMapper listMapper;

  @Autowired
  private SelectionItemMapper itemMapper;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private UserProfileMapper profileMapper;

  @Autowired
  private AuthService authService;

  /**
   * 当前用户的所有清单。
   *
   * @return 当前用户的清单列表
   */
  public List<SelectionList> myLists() {
    Long uid = authService.currentUser().getId();
    return listMapper.selectList(
        Wrappers.<SelectionList>lambdaQuery().eq(SelectionList::getUserId, uid));
  }

  /**
   * 新建清单。
   *
   * @param list 清单内容（自动回填当前用户ID）
   * @return 新建后的清单
   */
  @Transactional
  public SelectionList createList(SelectionList list) {
    list.setUserId(authService.currentUser().getId());
    listMapper.insert(list);
    return list;
  }

  /**
   * 清单下所有项（批量补型号名/品类名）。
   *
   * @param listId 清单ID
   * @return 清单项列表
   */
  public List<SelectionItemVO> items(Long listId) {
    List<SelectionItem> items = itemMapper.selectList(
        Wrappers.<SelectionItem>lambdaQuery().eq(SelectionItem::getListId, listId));
    if (items.isEmpty()) {
      return List.of();
    }
    Map<Long, Model> modelMap = batchModels(items);
    Map<Long, Category> catMap = batchCategories(items, modelMap);
    return items.stream().map(it -> toItemVO(it, modelMap, catMap)).toList();
  }

  /**
   * 新增清单项（自动算小计 = 数量 × 单价）。
   *
   * @param listId 所属清单ID
   * @param item   清单项内容
   * @return 新增后的清单项
   */
  @Transactional
  public SelectionItem addItem(Long listId, SelectionItem item) {
    item.setListId(listId);
    recalcTotal(item);
    itemMapper.insert(item);
    return item;
  }

  /**
   * 修改清单项。
   *
   * @param itemId 清单项ID
   * @param item   清单项内容
   * @return 更新后的清单项
   * @throws BizException 清单项不存在时抛出
   */
  @Transactional
  public SelectionItem updateItem(Long itemId, SelectionItem item) {
    SelectionItem exist = itemMapper.selectById(itemId);
    if (exist == null) {
      throw new BizException("清单项不存在");
    }
    item.setId(itemId);
    item.setListId(exist.getListId());
    recalcTotal(item);
    itemMapper.updateById(item);
    return item;
  }

  /**
   * 删除清单项。
   *
   * @param itemId 清单项ID
   */
  @Transactional
  public void deleteItem(Long itemId) {
    itemMapper.deleteById(itemId);
  }

  /**
   * 预算统计：总价 vs 总预算，按品类/空间分布。
   *
   * @param listId 清单ID
   * @return 预算统计结果
   */
  public BudgetVO budget(Long listId) {
    List<SelectionItem> items = itemMapper.selectList(
        Wrappers.<SelectionItem>lambdaQuery().eq(SelectionItem::getListId, listId));
    BigDecimal spent = items.stream()
        .map(SelectionItem::getTotalPrice)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BudgetVO vo = new BudgetVO();
    vo.setTotalSpent(spent);
    Long uid = authService.currentUser().getId();
    UserProfile p = profileMapper.selectOne(
        Wrappers.<UserProfile>lambdaQuery().eq(UserProfile::getUserId, uid));
    BigDecimal budget = (p == null || p.getTotalBudget() == null) ? BigDecimal.ZERO : p.getTotalBudget();
    vo.setTotalBudget(budget);
    BigDecimal remaining = budget.subtract(spent);
    vo.setRemaining(remaining);
    vo.setOverspent(remaining.signum() < 0);
    vo.setByCategory(groupByCategory(items, spent));
    vo.setBySpace(groupBySpace(items, spent));
    return vo;
  }

  /**
   * 导出清单为 Excel（直接写响应流）。
   *
   * @param listId   清单ID
   * @param response HTTP 响应，用于写出 Excel 文件流
   * @throws BizException 写出失败时抛出
   */
  public void export(Long listId, HttpServletResponse response) {
    List<SelectionItemVO> items = items(listId);
    List<Map<String, Object>> rows = items.stream().map(it -> {
      Map<String, Object> m = new LinkedHashMap<>();
      m.put("型号", it.getModelName());
      m.put("品类", it.getCategoryName());
      m.put("空间", it.getSpace());
      m.put("规格", it.getSpec());
      m.put("数量", it.getQuantity());
      m.put("单价", it.getUnitPrice());
      m.put("小计", it.getTotalPrice());
      m.put("渠道", it.getChannel());
      m.put("状态", it.getPurchaseStatus());
      return m;
    }).collect(Collectors.toList());
    try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
      writer.write(rows, true);
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", "attachment;filename=selection-" + listId + ".xlsx");
      writer.flush(response.getOutputStream(), true);
    } catch (IOException e) {
      log.error("导出清单失败 listId={}", listId, e);
      throw new BizException("导出失败");
    }
  }

  /**
   * 重算清单项小计 = 单价 × 数量（null 安全，缺省数量为 1）。
   *
   * @param item 待重算的清单项
   */
  void recalcTotal(SelectionItem item) {
    BigDecimal unit = item.getUnitPrice() == null ? BigDecimal.ZERO : item.getUnitPrice();
    int qty = item.getQuantity() == null ? 1 : item.getQuantity();
    item.setQuantity(qty);
    item.setTotalPrice(unit.multiply(BigDecimal.valueOf(qty)));
  }

  private List<BudgetGroupVO> groupByCategory(List<SelectionItem> items, BigDecimal spent) {
    Map<Long, BigDecimal> map = items.stream()
        .filter(i -> i.getCategoryId() != null && i.getTotalPrice() != null)
        .collect(Collectors.groupingBy(SelectionItem::getCategoryId,
            Collectors.reducing(BigDecimal.ZERO, SelectionItem::getTotalPrice, BigDecimal::add)));
    if (map.isEmpty()) {
      return List.of();
    }
    Map<Long, String> nameMap = categoryMapper.selectBatchIds(map.keySet()).stream()
        .collect(Collectors.toMap(Category::getId, Category::getName));
    return map.entrySet().stream()
        .map(e -> toGroup(nameMap.getOrDefault(e.getKey(), "未分类"), e.getValue(), spent))
        .toList();
  }

  private List<BudgetGroupVO> groupBySpace(List<SelectionItem> items, BigDecimal spent) {
    Map<String, BigDecimal> map = items.stream()
        .filter(i -> i.getSpace() != null && i.getTotalPrice() != null)
        .collect(Collectors.groupingBy(SelectionItem::getSpace,
            LinkedHashMap::new,
            Collectors.reducing(BigDecimal.ZERO, SelectionItem::getTotalPrice, BigDecimal::add)));
    return map.entrySet().stream()
        .map(e -> toGroup(e.getKey(), e.getValue(), spent))
        .toList();
  }

  /**
   * 组装预算分组：金额 + 占比百分比（HALF_UP 两位小数，花费为 0 时占比 0）。
   *
   * @param name   分组名称
   * @param amount 分组金额
   * @param spent  总花费（用于计算占比）
   * @return 预算分组
   */
  BudgetGroupVO toGroup(String name, BigDecimal amount, BigDecimal spent) {
    BudgetGroupVO g = new BudgetGroupVO();
    g.setName(name);
    g.setAmount(amount);
    g.setRatio(spent.signum() == 0 ? BigDecimal.ZERO
        : amount.multiply(HUNDRED).divide(spent, 2, RoundingMode.HALF_UP));
    return g;
  }

  private SelectionItemVO toItemVO(SelectionItem it, Map<Long, Model> modelMap, Map<Long, Category> catMap) {
    SelectionItemVO vo = new SelectionItemVO();
    BeanUtil.copyProperties(it, vo);
    Model m = modelMap.get(it.getModelId());
    if (m != null) {
      vo.setModelName(m.getName());
      if (it.getCategoryId() == null) {
        vo.setCategoryId(m.getCategoryId());
      }
    }
    Long catId = vo.getCategoryId();
    if (catId != null) {
      Category c = catMap.get(catId);
      if (c != null) {
        vo.setCategoryName(c.getName());
      }
    }
    return vo;
  }

  private Map<Long, Model> batchModels(List<SelectionItem> items) {
    List<Long> ids = items.stream().map(SelectionItem::getModelId).filter(Objects::nonNull).distinct().toList();
    if (ids.isEmpty()) {
      return Map.of();
    }
    return modelMapper.selectBatchIds(ids).stream()
        .collect(Collectors.toMap(Model::getId, Function.identity()));
  }

  private Map<Long, Category> batchCategories(List<SelectionItem> items, Map<Long, Model> modelMap) {
    List<Long> ids = items.stream()
        .map(i -> i.getCategoryId() != null ? i.getCategoryId()
            : (modelMap.get(i.getModelId()) != null ? modelMap.get(i.getModelId()).getCategoryId() : null))
        .filter(Objects::nonNull).distinct().toList();
    if (ids.isEmpty()) {
      return Map.of();
    }
    return categoryMapper.selectBatchIds(ids).stream()
        .collect(Collectors.toMap(Category::getId, Function.identity()));
  }
}
