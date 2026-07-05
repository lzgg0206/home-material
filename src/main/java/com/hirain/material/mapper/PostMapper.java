package com.hirain.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hirain.material.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内容源表数据访问。
 *
 * @author lingzhi.Wang
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
