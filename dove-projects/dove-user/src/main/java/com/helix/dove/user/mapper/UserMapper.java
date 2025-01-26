package com.helix.dove.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helix.dove.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Mapper Interface
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 