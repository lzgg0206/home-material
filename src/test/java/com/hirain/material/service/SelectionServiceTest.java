package com.hirain.material.service;

import com.hirain.material.entity.SelectionItem;
import com.hirain.material.entity.User;
import com.hirain.material.entity.UserProfile;
import com.hirain.material.mapper.CategoryMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.SelectionItemMapper;
import com.hirain.material.mapper.SelectionListMapper;
import com.hirain.material.mapper.UserProfileMapper;
import com.hirain.material.vo.BudgetGroupVO;
import com.hirain.material.vo.BudgetVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * {@link SelectionService} 预算计算与清单项小计单测（Mapper 全部 mock）。
 */
@ExtendWith(MockitoExtension.class)
class SelectionServiceTest {

  @Mock
  private SelectionListMapper listMapper;

  @Mock
  private SelectionItemMapper itemMapper;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private CategoryMapper categoryMapper;

  @Mock
  private UserProfileMapper profileMapper;

  @Mock
  private AuthService authService;

  @InjectMocks
  private SelectionService service;

  @Test
  void recalcTotal_normal() {
    SelectionItem it = new SelectionItem();
    it.setUnitPrice(new BigDecimal("99.50"));
    it.setQuantity(3);
    service.recalcTotal(it);
    assertEquals(new BigDecimal("298.50"), it.getTotalPrice());
  }

  @Test
  void recalcTotal_nullUnitPrice() {
    SelectionItem it = new SelectionItem();
    it.setQuantity(2);
    service.recalcTotal(it);
    assertEquals(BigDecimal.ZERO, it.getTotalPrice());
  }

  @Test
  void recalcTotal_nullQuantityDefaultsOne() {
    SelectionItem it = new SelectionItem();
    it.setUnitPrice(new BigDecimal("100"));
    service.recalcTotal(it);
    assertEquals(1, it.getQuantity());
    assertEquals(new BigDecimal("100"), it.getTotalPrice());
  }

  @Test
  void toGroup_zeroSpentRatioZero() {
    BudgetGroupVO g = service.toGroup("客厅", new BigDecimal("100"), BigDecimal.ZERO);
    assertEquals("客厅", g.getName());
    assertEquals(0, g.getRatio().signum());
  }

  @Test
  void toGroup_ratioHalfUp() {
    // 100 / 300 * 100 = 33.33（验证 HALF_UP 两位小数）
    BudgetGroupVO g = service.toGroup("厨房", new BigDecimal("100"), new BigDecimal("300"));
    assertEquals(new BigDecimal("33.33"), g.getRatio());
  }

  @Test
  void budget_normalNotOverspent() {
    SelectionItem a = item("客厅", new BigDecimal("100"));
    SelectionItem b = item("厨房", new BigDecimal("200"));
    when(itemMapper.selectList(any())).thenReturn(List.of(a, b));
    when(authService.currentUser()).thenReturn(user(1L));
    UserProfile p = new UserProfile();
    p.setTotalBudget(new BigDecimal("400"));
    when(profileMapper.selectOne(any())).thenReturn(p);

    BudgetVO vo = service.budget(1L);

    assertEquals(new BigDecimal("300"), vo.getTotalSpent());
    assertEquals(new BigDecimal("400"), vo.getTotalBudget());
    assertEquals(new BigDecimal("100"), vo.getRemaining());
    assertFalse(vo.isOverspent());
    assertEquals(2, vo.getBySpace().size());
    BigDecimal livingRatio = vo.getBySpace().stream()
        .filter(g -> "客厅".equals(g.getName())).findFirst().orElseThrow().getRatio();
    assertEquals(new BigDecimal("33.33"), livingRatio);
  }

  @Test
  void budget_overspent() {
    SelectionItem a = item("客厅", new BigDecimal("300"));
    when(itemMapper.selectList(any())).thenReturn(List.of(a));
    when(authService.currentUser()).thenReturn(user(1L));
    UserProfile p = new UserProfile();
    p.setTotalBudget(new BigDecimal("200"));
    when(profileMapper.selectOne(any())).thenReturn(p);

    BudgetVO vo = service.budget(1L);

    assertTrue(vo.isOverspent());
    assertEquals(new BigDecimal("-100"), vo.getRemaining());
  }

  private SelectionItem item(String space, BigDecimal total) {
    SelectionItem it = new SelectionItem();
    it.setSpace(space);
    it.setTotalPrice(total);
    return it;
  }

  private User user(long id) {
    User u = new User();
    u.setId(id);
    return u;
  }
}
