package com.hirain.material.service;

import com.hirain.material.entity.Brand;
import com.hirain.material.mapper.BrandMapper;
import com.hirain.material.mapper.ModelMapper;
import com.hirain.material.mapper.ModelReputationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link BrandService} 标签生成单测（Mapper 全部 mock）。
 */
@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

  @Mock
  private BrandMapper brandMapper;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private ModelReputationMapper reputationMapper;

  @InjectMocks
  private BrandService service;

  @Test
  void buildTags_highDomesticWithPraise() {
    Brand b = new Brand();
    b.setTier("high");
    b.setOrigin("domestic");
    b.setPraiseRate(new BigDecimal("98.7"));
    assertEquals(List.of("高端品质", "国产", "好评98%"), service.buildTags(b));
  }

  @Test
  void buildTags_midImported() {
    Brand b = new Brand();
    b.setTier("mid");
    b.setOrigin("imported");
    b.setPraiseRate(new BigDecimal("95"));
    List<String> tags = service.buildTags(b);
    assertEquals("中端主流", tags.get(0));
    assertEquals("进口", tags.get(1));
    assertEquals("好评95%", tags.get(2));
  }

  @Test
  void buildTags_nullPraiseYieldsOnlyTwoTags() {
    Brand b = new Brand();
    b.setTier("entry");
    b.setOrigin("domestic");
    assertEquals(List.of("入门性价比", "国产"), service.buildTags(b));
  }
}
