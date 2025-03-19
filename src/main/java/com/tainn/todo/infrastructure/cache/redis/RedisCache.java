package com.tainn.todo.infrastructure.cache.redis;

import java.util.concurrent.TimeUnit;

public interface RedisCache {
    void setString(String key, String value);

    String getString(String key);

    void setObject(String key, Object value);

    void setObject(String key, Object value, Long ttl, TimeUnit timeUnit);

    <T> T getObject(String key, Class<T> targetClass);

    void delete(String key);

    boolean hasKey(String key);

    Long increment(String key, long liveTime);
}
