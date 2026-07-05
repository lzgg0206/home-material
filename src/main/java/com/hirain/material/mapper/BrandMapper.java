package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Brand;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {
}
