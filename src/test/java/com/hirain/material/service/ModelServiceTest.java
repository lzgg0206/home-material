package com.hirain.material.service;

import com.hirain.material.entity.ModelPitfall;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.ModelKeywordMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.ModelPitfallMapper;
import com.hirain.material.mapper.ModelReputationMapper;
import com.hirain.material.vo.PitfallGroupVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ModelService} 踩坑类型映射/标签拆分/分组单测（Mapper 全部 mock）。
 */
@ExtendWith(MockitoExtension.class)
class ModelServiceTest {

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private BrandMapper brandMapper;

  @Mock
  private ModelReputationMapper reputationMapper;

  @Mock
  private ModelKeywordMapper keywordMapper;

  @Mock
  private ModelPitfallMapper pitfallMapper;

  @InjectMocks
  private ModelService service;

  @Test
  void typeName_knownTypes() {
    assertEquals("质量问题", service.typeName("quality"));
    assertEquals("安装售后", service.typeName("install"));
    assertEquals("宣传不符", service.typeName("mismatch"));
    assertEquals("使用体验", service.typeName("experience"));
  }

  @Test
  void typeName_unknownAndNullFallback() {
    assertEquals("其他", service.typeName("xyz"));
    assertEquals("其他", service.typeName(null));
  }

  @Test
  void splitTags_trimsAndDropsBlank() {
    assertEquals(List.of("a", "b", "c"), service.splitTags("a, b ,c"));
  }

  @Test
  void splitTags_blankAndNull() {
    assertTrue(service.splitTags("").isEmpty());
    assertTrue(service.splitTags(null).isEmpty());
    assertTrue(service.splitTags(" , ").isEmpty());
  }

  @Test
  void groupPitfalls_groupsByTypeWithChineseName() {
    ModelPitfall p1 = pitfall(1L, "quality", "起皮", 1);
    ModelPitfall p2 = pitfall(2L, "quality", "开裂", 0);
    ModelPitfall p3 = pitfall(3L, "install", "安装费", 1);

    List<PitfallGroupVO> groups = service.groupPitfalls(List.of(p1, p2, p3));

    assertEquals(2, groups.size());
    PitfallGroupVO qualityGroup = groups.stream()
        .filter(g -> "quality".equals(g.getType())).findFirst().orElseThrow();
    assertEquals("质量问题", qualityGroup.getTypeName());
    assertEquals(2, qualityGroup.getItems().size());
  }

  private ModelPitfall pitfall(long id, String type, String desc, int highRisk) {
    ModelPitfall p = new ModelPitfall();
    p.setId(id);
    p.setType(type);
    p.setDescription(desc);
    p.setIsHighRisk(highRisk);
    return p;
  }
}
