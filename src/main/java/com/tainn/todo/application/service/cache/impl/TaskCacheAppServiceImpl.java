package com.tainn.todo.application.service.cache.impl;

import com.tainn.todo.application.service.cache.TaskCacheAppService;
import com.tainn.todo.domain.model.dto.response.TaskResponse;
import com.tainn.todo.domain.model.entity.Task;
import com.tainn.todo.domain.model.mapper.TaskMapper;
import com.tainn.todo.domain.service.TaskService;
import com.tainn.todo.infrastructure.cache.local.LocalCache;
import com.tainn.todo.infrastructure.cache.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class TaskCacheAppServiceImpl implements TaskCacheAppService {
    TaskService service;
    TaskMapper mapper;
    LocalCache<TaskResponse> localCache;
    RedisCache redisCache;

    @Override
    public TaskResponse getTaskByCache(Long id) {
        TaskResponse response = getTaskFromLocalCache(id);
        if (response != null) {
            return response;
        }

        String cacheKey = "TASK:" + id;
        response = getTaskFromRedisCache(id, cacheKey);
        if (response != null) {
            return response;
        }

        return getTaskFromDatabase(id, cacheKey);
    }

    private TaskResponse getTaskFromLocalCache(Long homestayId) {
        TaskResponse response = localCache.getIfPresent(homestayId);
        if (response != null) {
            log.info("GET TASK {} FROM LOCAL CACHE", homestayId);
        }
        return response;
    }

    private TaskResponse getTaskFromRedisCache(Long homestayId, String cacheKey) {
        TaskResponse response = redisCache.getObject(cacheKey, TaskResponse.class);
        if (response != null) {
            log.info("GET TASK {} FROM REDIS CACHE", homestayId);
            localCache.put(homestayId, response);
        }
        return response;
    }

    private TaskResponse getTaskById(Long homestayId) {
        Task response = service.getById(homestayId);
        log.info("GET TASK {} FROM DATABASE", homestayId);
        return mapper.toDTO(response);
    }

    private TaskResponse getTaskFromDatabase(Long taskId, String cacheKey) {
        TaskResponse response = getTaskById(taskId);
        redisCache.setObject(cacheKey, response, 1L, TimeUnit.HOURS);
        localCache.put(taskId, response);
        return response;
    }
}
