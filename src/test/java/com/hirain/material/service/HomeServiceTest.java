package com.hirain.material.service;

import com.hirain.material.entity.Brand;
import com.hirain.material.entity.Category;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.CategoryMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.ModelPitfallMapper;
import com.hirain.material.vo.BrandRankingVO;
import com.hirain.material.vo.CategoryRanking;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * {@link HomeService} 排名/品类匹配/榜单聚合单测（Mapper 全部 mock）。
 */
@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

  @Mock
  private CategoryMapper categoryMapper;

  @Mock
  private BrandMapper brandMapper;

  @Mock
  private ModelPitfallMapper pitfallMapper;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private HomeService service;

  @Test
  void toRanking_assignsSequentialRankFromOne() {
    Brand b1 = brandOf("东鹏", "1", new BigDecimal("99"));
    Brand b2 = brandOf("马可", "1", new BigDecimal("95"));
    List<BrandRankingVO> r = service.toRanking(List.of(b1, b2));
    assertEquals(2, r.size());
    assertEquals(1, r.get(0).getRank());
    assertEquals(2, r.get(1).getRank());
    assertEquals("东鹏", r.get(0).getName());
  }

  @Test
  void toRanking_emptyList() {
    assertTrue(service.toRanking(List.of()).isEmpty());
  }

  @Test
  void belongsToCategory_preciseMatchNotLike() {
    // 回归：mainCategoryIds="1,10,11"，cid=1 命中、cid=10 命中、cid=2 不命中
    // 旧的 LIKE '%1%' 会把 cid=1 错误匹配含 10/11 的记录，这里锁死边界
    Brand b = new Brand();
    b.setMainCategoryIds("1,10,11");
    assertTrue(service.belongsToCategory(b, 1L));
    assertTrue(service.belongsToCategory(b, 10L));
    assertFalse(service.belongsToCategory(b, 2L));
  }

  @Test
  void belongsToCategory_nullAndBlank() {
    Brand b = new Brand();
    b.setMainCategoryIds(null);
    assertFalse(service.belongsToCategory(b, 1L));
    b.setMainCategoryIds("   ");
    assertFalse(service.belongsToCategory(b, 1L));
  }

  @Test
  void buildTopRankings_groupsByCategoryAndKeepsDeclaredOrder() {
    // selectBatchIds 不保证顺序，模拟乱序返回，验证按 HOME_CATEGORY_IDS=[10,13,16] 排序输出
    when(categoryMapper.selectBatchIds(anyList())).thenReturn(List.of(
        category(16L, "沙发"), category(10L, "瓷砖"), category(13L, "橱柜")));
    // B 的 mainCategoryIds="10,13" 应同时归入瓷砖组和橱柜组（精确匹配，不丢不误判）
    when(brandMapper.selectList(any())).thenReturn(List.of(
        brandOf("东鹏", "10", new BigDecimal("99")),
        brandOf("诺贝尔", "10,13", new BigDecimal("90")),
        brandOf("A家", "13", new BigDecimal("80"))));

    List<CategoryRanking> r = service.buildTopRankings();

    assertEquals(3, r.size());
    assertEquals("瓷砖", r.get(0).getCategoryName());
    assertEquals("橱柜", r.get(1).getCategoryName());
    assertEquals("沙发", r.get(2).getCategoryName());
    // cid=10 瓷砖组两条（东鹏、诺贝尔），rank 从 1 开始
    assertEquals(2, r.get(0).getTop3().size());
    assertEquals(1, r.get(0).getTop3().get(0).getRank());
    assertEquals(2, r.get(0).getTop3().get(1).getRank());
    // cid=13 橱柜组两条（诺贝尔、A家），cid=16 沙发组空
    assertEquals(2, r.get(1).getTop3().size());
    assertTrue(r.get(2).getTop3().isEmpty());
  }

  private Brand brandOf(String name, String cids, BigDecimal praise) {
    Brand b = new Brand();
    b.setName(name);
    b.setMainCategoryIds(cids);
    b.setPraiseRate(praise);
    return b;
  }

  private Category category(long id, String name) {
    Category c = new Category();
    c.setId(id);
    c.setName(name);
    return c;
  }
}
