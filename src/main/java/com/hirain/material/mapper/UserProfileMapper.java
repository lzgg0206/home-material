package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 家装档案表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}
