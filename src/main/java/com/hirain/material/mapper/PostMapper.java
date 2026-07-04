package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/** 内容源 Mapper */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
