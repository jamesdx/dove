package com.helix.dove.auth.service;

import java.util.Map;

/**
 * 配置服务接口
 * 负责管理系统配置、安全配置和区域配置等
 */
public interface ConfigService {
    
    /**
     * 获取系统配置
     * 包括系统基础配置、性能配置、功能开关等
     *
     * @return 系统配置Map
     */
    Map<String, Object> getSystemConfigs();
    
    /**
     * 获取安全配置
     * 包括密码策略、会话策略、认证策略等
     *
     * @return 安全配置Map
     */
    Map<String, Object> getSecurityConfigs();
    
    /**
     * 获取区域配置
     * 包括区域信息、路由策略、负载均衡策略等
     *
     * @return 区域配置Map
     */
    Map<String, Object> getRegionConfigs();
} 