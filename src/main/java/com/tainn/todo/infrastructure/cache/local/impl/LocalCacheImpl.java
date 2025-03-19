package com.tainn.todo.infrastructure.cache.local.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tainn.todo.infrastructure.cache.local.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LocalCacheImpl<T> implements LocalCache<T> {
    private final Cache<Long, T> localCache = CacheBuilder.newBuilder()
            .initialCapacity(12)
            .concurrencyLevel(6) // cpu: số lõi
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    @Override
    public void put(long key, T value) {
        localCache.put(key, value);
    }

    @Override
    public T getIfPresent(long key) {
        return localCache.getIfPresent(key);
    }

    @Override
    public void invalidate(long key) {
        localCache.invalidate(key);
    }
}
