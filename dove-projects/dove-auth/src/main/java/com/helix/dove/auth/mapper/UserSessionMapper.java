package com.helix.dove.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helix.dove.auth.entity.UserSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Session Mapper Interface
 */
@Mapper
public interface UserSessionMapper extends BaseMapper<UserSession> {
} 