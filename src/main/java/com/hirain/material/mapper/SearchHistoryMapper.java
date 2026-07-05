package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.SearchHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搜索历史表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {
}
