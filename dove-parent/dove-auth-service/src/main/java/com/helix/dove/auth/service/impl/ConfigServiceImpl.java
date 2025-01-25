package com.helix.dove.auth.service.impl;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helix.dove.auth.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final com.alibaba.nacos.api.config.ConfigService nacosConfigService;
    private final ObjectMapper objectMapper;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    private static final String CONFIG_GROUP = "DEFAULT_GROUP";
    private static final String SYSTEM_CONFIG_DATA_ID = "system-config.yaml";
    private static final String SECURITY_CONFIG_DATA_ID = "security-config.yaml";
    private static final String REGION_CONFIG_DATA_ID = "region-config.yaml";
    private static final long CONFIG_TIMEOUT_MS = 3000;

    @Override
    public Map<String, Object> getSystemConfigs() {
        try {
            String config = nacosConfigService.getConfig(
                SYSTEM_CONFIG_DATA_ID,
                CONFIG_GROUP,
                CONFIG_TIMEOUT_MS
            );
            return parseConfig(config);
        } catch (Exception e) {
            log.error("Failed to get system configs from Nacos", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getSecurityConfigs() {
        try {
            String config = nacosConfigService.getConfig(
                SECURITY_CONFIG_DATA_ID,
                CONFIG_GROUP,
                CONFIG_TIMEOUT_MS
            );
            return parseConfig(config);
        } catch (Exception e) {
            log.error("Failed to get security configs from Nacos", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getRegionConfigs() {
        try {
            String config = nacosConfigService.getConfig(
                REGION_CONFIG_DATA_ID,
                CONFIG_GROUP,
                CONFIG_TIMEOUT_MS
            );
            return parseConfig(config);
        } catch (Exception e) {
            log.error("Failed to get region configs from Nacos", e);
            return new HashMap<>();
        }
    }

    private Map<String, Object> parseConfig(String config) {
        try {
            if (config == null || config.trim().isEmpty()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(config, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("Failed to parse config", e);
            return new HashMap<>();
        }
    }

    /**
     * 添加配置监听器
     * @param dataId 配置ID
     * @param listener 监听器
     */
    public void addConfigListener(String dataId, com.alibaba.nacos.api.config.listener.Listener listener) {
        try {
            nacosConfigService.addListener(dataId, CONFIG_GROUP, listener);
        } catch (NacosException e) {
            log.error("Failed to add config listener for dataId: " + dataId, e);
        }
    }

    /**
     * 发布配置
     * @param dataId 配置ID
     * @param content 配置内容
     * @return 是否发布成功
     */
    public boolean publishConfig(String dataId, String content) {
        try {
            return nacosConfigService.publishConfig(
                dataId,
                CONFIG_GROUP,
                content,
                ConfigType.YAML.getType()
            );
        } catch (NacosException e) {
            log.error("Failed to publish config for dataId: " + dataId, e);
            return false;
        }
    }

    /**
     * 删除配置
     * @param dataId 配置ID
     * @return 是否删除成功
     */
    public boolean removeConfig(String dataId) {
        try {
            return nacosConfigService.removeConfig(dataId, CONFIG_GROUP);
        } catch (NacosException e) {
            log.error("Failed to remove config for dataId: " + dataId, e);
            return false;
        }
    }
} 