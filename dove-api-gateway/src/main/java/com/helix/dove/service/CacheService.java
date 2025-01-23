package com.helix.dove.service;

import reactor.core.publisher.Mono;

public interface CacheService {
    <T> Mono<T> get(String key, Class<T> type);
    <T> Mono<Void> put(String key, T value);
    <T> Mono<T> getOrFetch(String key, Class<T> type, Mono<T> supplier);
    Mono<Void> delete(String key);
    Mono<Void> clear();
    Mono<Long> size();
    void clearCache(String key);
    void refreshCache(String key);
}