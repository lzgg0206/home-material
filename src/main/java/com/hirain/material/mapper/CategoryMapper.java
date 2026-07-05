package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品类表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
