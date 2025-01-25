package com.helix.dove.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Configuration
public class RegionConfig {

    @Value("${spring.cloud.nacos.discovery.metadata.region:default}")
    private String region;

    @Bean
    public String currentRegion() {
        return region;
    }

    @Bean
    public RegionResolver regionResolver(Environment env) {
        return new RegionResolver(env);
    }
}

class RegionResolver {
    private final Environment env;
    private static final String REGION_PROPERTY = "spring.cloud.nacos.discovery.metadata.region";

    public RegionResolver(Environment env) {
        this.env = env;
    }

    public String resolveRegion() {
        String region = env.getProperty(REGION_PROPERTY);
        if (region == null) {
            // 根据服务器IP或其他信息自动判断区域
            region = determineRegionFromIp();
        }
        return region;
    }

    private String determineRegionFromIp() {
        // TODO: 实现根据IP地址判断区域的逻辑
        return "default";
    }

    public boolean isSameRegion(String otherRegion) {
        return resolveRegion().equals(otherRegion);
    }

    public int calculateRegionPriority(String targetRegion) {
        if (isSameRegion(targetRegion)) {
            return 100;
        }
        // 根据区域的距离计算优先级
        return 50;
    }
} 