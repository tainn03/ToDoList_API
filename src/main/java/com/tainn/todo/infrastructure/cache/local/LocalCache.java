package com.tainn.todo.infrastructure.cache.local;

public interface LocalCache<T> {
    void put(long key, T value);

    T getIfPresent(long key);

    void invalidate(long key);
}
