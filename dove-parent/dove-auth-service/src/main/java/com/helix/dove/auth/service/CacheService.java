package com.helix.dove.auth.service;

public interface CacheService {
    
    /**
     * 预热缓存
     */
    void warmUpCache();
    
    /**
     * 清理过期缓存
     */
    void cleanExpiredCache();
    
    /**
     * 刷新缓存
     *
     * @param key 缓存键
     */
    void refreshCache(String key);
    
    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计信息
     */
    CacheStats getCacheStats();
}

record CacheStats(
    long hitCount,
    long missCount,
    long loadCount,
    double hitRate,
    double missRate,
    long totalLoadTime,
    long evictionCount
) {} 