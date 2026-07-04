package com.hirain.material.service;

import com.hirain.material.entity.Category;
import com.hirain.material.mapper.CategoryMapper;
import com.hirain.material.vo.CategoryTreeVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * {@link CategoryService} 品类树构建单测（Mapper 全部 mock）。
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryService service;

  @Test
  void getTree_nullAndZeroParentBothTreatedAsRoot() {
    // parent_id 为 null 与显式 0 都应视为一级
    when(categoryMapper.selectList(any())).thenReturn(List.of(
        category(1L, "硬装", 0L, 1),
        category(2L, "软装", null, 2),
        category(11L, "瓷砖", 1L, 3)));

    List<CategoryTreeVO> tree = service.getTree();

    assertEquals(2, tree.size());
    assertEquals("硬装", tree.get(0).getName());
    assertEquals(1, tree.get(0).getChildren().size());
    assertEquals("瓷砖", tree.get(0).getChildren().get(0).getName());
  }

  @Test
  void getTree_empty() {
    when(categoryMapper.selectList(any())).thenReturn(List.of());
    assertTrue(service.getTree().isEmpty());
  }

  private Category category(long id, String name, Long parentId, int sort) {
    Category c = new Category();
    c.setId(id);
    c.setName(name);
    c.setParentId(parentId);
    c.setSort(sort);
    return c;
  }
}
