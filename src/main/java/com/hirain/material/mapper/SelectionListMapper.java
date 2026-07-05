package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.SelectionList;
import org.apache.ibatis.annotations.Mapper;

/**
 * 自选清单表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface SelectionListMapper extends BaseMapper<SelectionList> {
}
