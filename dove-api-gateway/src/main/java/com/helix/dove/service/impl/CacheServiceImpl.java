package com.helix.dove.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.helix.dove.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private Cache<String, Object> localCache;

    @PostConstruct
    public void init() {
        localCache = Caffeine.newBuilder()
                .maximumSize(10000)
                .build();
    }

    @Override
    public <T> Mono<T> get(String key, Class<T> type) {
        T value = type.cast(localCache.getIfPresent(key));
        if (value != null) {
            return Mono.just(value);
        }
        return redisTemplate.opsForValue()
                .get(key)
                .map(obj -> {
                    T result = type.cast(obj);
                    localCache.put(key, result);
                    return result;
                });
    }

    @Override
    public <T> Mono<Void> put(String key, T value) {
        localCache.put(key, value);
        return redisTemplate.opsForValue()
                .set(key, value)
                .then();
    }

    @Override
    public <T> Mono<T> getOrFetch(String key, Class<T> type, Mono<T> supplier) {
        return get(key, type)
                .switchIfEmpty(supplier.flatMap(value -> 
                    put(key, value).thenReturn(value)
                ));
    }

    @Override
    public Mono<Void> delete(String key) {
        localCache.invalidate(key);
        return redisTemplate.delete(key).then();
    }

    @Override
    public Mono<Void> clear() {
        localCache.invalidateAll();
        return redisTemplate.execute(connection -> connection.serverCommands().flushDb()).then();
    }

    @Override
    public Mono<Long> size() {
        return redisTemplate.keys("*").count();
    }

    @Override
    public void clearCache(String key) {
        // Implementation
    }

    @Override
    public void refreshCache(String key) {
        // Implementation
    }
}