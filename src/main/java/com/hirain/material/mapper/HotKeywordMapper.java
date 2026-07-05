package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.HotKeyword;
import org.apache.ibatis.annotations.Mapper;

/**
 * 热搜词表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface HotKeywordMapper extends BaseMapper<HotKeyword> {
}
