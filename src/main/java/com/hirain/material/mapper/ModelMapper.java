package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Model;
import org.apache.ibatis.annotations.Mapper;

/**
 * 型号表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface ModelMapper extends BaseMapper<Model> {
}
