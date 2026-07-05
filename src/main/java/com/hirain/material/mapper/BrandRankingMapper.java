package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.BrandRanking;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌排行榜表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface BrandRankingMapper extends BaseMapper<BrandRanking> {
}
