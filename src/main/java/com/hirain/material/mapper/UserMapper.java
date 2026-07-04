package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.User;
import org.apache.ibatis.annotations.Mapper;

/** 用户 Mapper */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
