package com.helix.dove.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.helix.dove.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private Cache<String, Object> localCache;

    @PostConstruct
    public void init() {
        localCache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();
    }

    @Override
    public <T> Mono<T> get(String key, Class<T> type) {
        Object localValue = localCache.getIfPresent(key);
        if (localValue != null && type.isInstance(localValue)) {
            return Mono.just(type.cast(localValue));
        }

        return redisTemplate.opsForValue().get(key)
                .filter(type::isInstance)
                .map(type::cast)
                .doOnNext(value -> localCache.put(key, value));
    }

    @Override
    public <T> Mono<Void> put(String key, T value) {
        localCache.put(key, value);
        return redisTemplate.opsForValue().set(key, value, Duration.ofHours(1))
                .then();
    }

    @Override
    public <T> Mono<T> getOrCreate(String key, Class<T> type, Mono<T> creator) {
        return get(key, type)
                .switchIfEmpty(creator.flatMap(value -> 
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
        return redisTemplate.delete(redisTemplate.keys("*")).then();
    }

    @Override
    public Mono<Long> size() {
        return redisTemplate.keys("*").count();
    }
}