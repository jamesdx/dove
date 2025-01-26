package com.helix.dove.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helix.dove.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Mapper Interface
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 