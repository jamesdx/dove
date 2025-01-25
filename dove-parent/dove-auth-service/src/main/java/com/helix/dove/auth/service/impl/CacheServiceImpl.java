package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.service.CacheService;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.auth.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final ConfigService configService;
    
    private final AtomicLong hitCount = new AtomicLong();
    private final AtomicLong missCount = new AtomicLong();
    private final AtomicLong loadCount = new AtomicLong();
    private final AtomicLong totalLoadTime = new AtomicLong();
    private final AtomicLong evictionCount = new AtomicLong();

    private static final String USER_CACHE_PREFIX = "user:";
    private static final String CONFIG_CACHE_PREFIX = "config:";
    private static final int CACHE_TTL_HOURS = 24;

    @Override
    public void warmUpCache() {
        log.info("Starting cache warm-up");
        try {
            long startTime = System.currentTimeMillis();
            
            // 预热用户缓存
            warmUpUserCache();
            // 预热配置缓存
            warmUpConfigCache();
            
            long endTime = System.currentTimeMillis();
            totalLoadTime.addAndGet(endTime - startTime);
            log.info("Cache warm-up completed in {} ms", endTime - startTime);
        } catch (Exception e) {
            log.error("Cache warm-up failed", e);
        }
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // 每小时执行一次
    public void cleanExpiredCache() {
        log.info("Starting expired cache cleanup");
        try {
            Set<String> keys = redisTemplate.keys("*");
            if (keys != null) {
                for (String key : keys) {
                    Long ttl = redisTemplate.getExpire(key);
                    if (ttl != null && ttl <= 0) {
                        redisTemplate.delete(key);
                        evictionCount.incrementAndGet();
                    }
                }
            }
            log.info("Expired cache cleanup completed");
        } catch (Exception e) {
            log.error("Expired cache cleanup failed", e);
        }
    }

    @Override
    public void refreshCache(String key) {
        log.info("Refreshing cache for key: {}", key);
        try {
            Objects.requireNonNull(cacheManager.getCache(key)).clear();
            loadCount.incrementAndGet();
            
            // 根据key类型执行不同的刷新逻辑
            if (key.startsWith(USER_CACHE_PREFIX)) {
                warmUpUserCache();
            } else if (key.startsWith(CONFIG_CACHE_PREFIX)) {
                warmUpConfigCache();
            }
            
            log.info("Cache refreshed for key: {}", key);
        } catch (Exception e) {
            log.error("Cache refresh failed for key: " + key, e);
        }
    }

    @Override
    public CacheStats getCacheStats() {
        long hits = hitCount.get();
        long misses = missCount.get();
        long loads = loadCount.get();
        double total = hits + misses;
        
        return new CacheStats(
            hits,
            misses,
            loads,
            total > 0 ? hits / total : 0.0,
            total > 0 ? misses / total : 0.0,
            totalLoadTime.get(),
            evictionCount.get()
        );
    }

    private void warmUpUserCache() {
        log.info("Warming up user cache");
        try {
            // 获取活跃用户列表
            List<Long> activeUserIds = userService.getActiveUserIds();
            
            for (Long userId : activeUserIds) {
                String cacheKey = USER_CACHE_PREFIX + userId;
                // 预加载用户基本信息
                userService.getUserById(userId).ifPresent(user -> 
                    redisTemplate.opsForValue().set(cacheKey, user, CACHE_TTL_HOURS, TimeUnit.HOURS));
                // 预加载用户权限信息
                userService.getUserPermissions(userId).ifPresent(permissions ->
                    redisTemplate.opsForValue().set(cacheKey + ":permissions", permissions, CACHE_TTL_HOURS, TimeUnit.HOURS));
                loadCount.incrementAndGet();
            }
            
            log.info("User cache warm-up completed for {} users", activeUserIds.size());
        } catch (Exception e) {
            log.error("User cache warm-up failed", e);
        }
    }

    private void warmUpConfigCache() {
        log.info("Warming up config cache");
        try {
            // 预加载系统配置
            loadSystemConfigs();
            // 预加载安全配置
            loadSecurityConfigs();
            // 预加载区域配置
            loadRegionConfigs();
            
            log.info("Config cache warm-up completed");
        } catch (Exception e) {
            log.error("Config cache warm-up failed", e);
        }
    }
    
    private void loadSystemConfigs() {
        String cacheKey = CONFIG_CACHE_PREFIX + "system";
        try {
            Map<String, Object> systemConfigs = configService.getSystemConfigs();
            redisTemplate.opsForHash().putAll(cacheKey, systemConfigs);
            redisTemplate.expire(cacheKey, CACHE_TTL_HOURS, TimeUnit.HOURS);
            loadCount.incrementAndGet();
            log.info("System configs loaded into cache");
        } catch (Exception e) {
            log.error("Failed to load system configs", e);
        }
    }
    
    private void loadSecurityConfigs() {
        String cacheKey = CONFIG_CACHE_PREFIX + "security";
        try {
            Map<String, Object> securityConfigs = configService.getSecurityConfigs();
            redisTemplate.opsForHash().putAll(cacheKey, securityConfigs);
            redisTemplate.expire(cacheKey, CACHE_TTL_HOURS, TimeUnit.HOURS);
            loadCount.incrementAndGet();
            log.info("Security configs loaded into cache");
        } catch (Exception e) {
            log.error("Failed to load security configs", e);
        }
    }
    
    private void loadRegionConfigs() {
        String cacheKey = CONFIG_CACHE_PREFIX + "region";
        try {
            Map<String, Object> regionConfigs = configService.getRegionConfigs();
            redisTemplate.opsForHash().putAll(cacheKey, regionConfigs);
            redisTemplate.expire(cacheKey, CACHE_TTL_HOURS, TimeUnit.HOURS);
            loadCount.incrementAndGet();
            log.info("Region configs loaded into cache");
        } catch (Exception e) {
            log.error("Failed to load region configs", e);
        }
    }
} 