package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
