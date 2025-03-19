package com.tainn.todo.infrastructure.cache.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tainn.todo.infrastructure.cache.redis.RedisCache;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisCacheImpl implements RedisCache {
    private final ObjectMapper objectMapper;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    public RedisCacheImpl() {
        // Khởi tạo ObjectMapper với module JavaTimeModule để chuyển đổi thời gian
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void setString(String key, String value) {
        if (!StringUtils.hasLength(key)) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        if (!StringUtils.hasLength(key)) {
            return;
        }
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
        } catch (Exception e) {
            log.error("ERROR WHEN SET OBJECT TO REDIS: {}", e.getMessage());
        }
    }

    @Override
    public void setObject(String key, Object value, Long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
        if (!StringUtils.hasLength(key)) {
            return;
        }
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, timeUnit);
        } catch (Exception e) {
            log.error("ERROR WHEN SET OBJECT TO REDIS: {}", e.getMessage());
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        switch (result) {
            case null -> {
                return null;
            }
            case Map map -> {
                try {
                    // Nếu kết quả là một LinkedHashMap, chuyển đổi nó thành đối tượng mục tiêu
                    return objectMapper.convertValue(result, targetClass);
                } catch (IllegalArgumentException e) {
                    log.error("ERROR WHEN DESERIALIZE MAP TO OBJECT: {}", e.getMessage());
                    return null;
                }
            }
            case String s -> {
                try {
                    // Nếu result là String, thực hiện chuyển đổi bình thường
                    return objectMapper.readValue((String) result, targetClass);
                } catch (JsonProcessingException e) {
                    log.error("ERROR WHEN DESERIALIZE STRING TO OBJECT: {}", e.getMessage());
                    return null;
                }
            }
            default -> {
            }
        }
        return null; // hoặc ném ra một ngoại lệ tùy ý
    }

    @Override
    public void delete(String key) {
        if (hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public Long increment(String key, long liveTime) {
        RedisAtomicLong atomicCounter = new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long counterValue = atomicCounter.getAndIncrement();
        //Thiết lập thời gian hết hạn ban đầu
        if (counterValue == 0 && liveTime > 0) {
            atomicCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return counterValue;
    }
}
