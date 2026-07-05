package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 品牌表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {

  /**
   * 统计某品牌下所有型号的踩坑反馈总数。
   *
   * @param brandId 品牌ID
   * @return 踩坑总数（无数据返回 0）
   */
  @Select("SELECT COALESCE(SUM(mp.count), 0) FROM hm_model_pitfall mp "
      + "JOIN hm_model m ON mp.model_id = m.id WHERE m.brand_id = #{brandId}")
  int sumPitfallCount(@Param("brandId") Long brandId);

  /**
   * 计算某品牌下所有型号的平均好评率。
   *
   * @param brandId 品牌ID
   * @return 平均好评率（无数据返回 null）
   */
  @Select("SELECT AVG(mr.praise_rate) FROM hm_model_reputation mr "
      + "JOIN hm_model m ON mr.model_id = m.id WHERE m.brand_id = #{brandId}")
  BigDecimal avgPraiseRate(@Param("brandId") Long brandId);
}
