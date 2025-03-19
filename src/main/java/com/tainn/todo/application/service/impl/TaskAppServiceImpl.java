package com.tainn.todo.application.service.impl;

import com.tainn.todo.application.service.TaskAppService;
import com.tainn.todo.application.service.cache.TaskCacheAppService;
import com.tainn.todo.domain.model.dto.request.TaskRequest;
import com.tainn.todo.domain.model.dto.response.TaskResponse;
import com.tainn.todo.domain.model.entity.Task;
import com.tainn.todo.domain.model.exception.BusinessException;
import com.tainn.todo.domain.model.exception.ErrorCode;
import com.tainn.todo.domain.model.mapper.TaskMapper;
import com.tainn.todo.domain.model.vo.TaskStatus;
import com.tainn.todo.domain.service.TaskService;
import com.tainn.todo.infrastructure.cache.local.LocalCache;
import com.tainn.todo.infrastructure.cache.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class TaskAppServiceImpl implements TaskAppService {
    TaskService service;
    TaskCacheAppService taskCacheService;
    TaskMapper mapper;
    LocalCache<TaskResponse> localCache;
    RedisCache redisCache;


    @Override
    public TaskResponse getById(Long id) {
        return taskCacheService.getTask(id);
    }

    @Override
    public Page<TaskResponse> getAll(int page, int size, String sort, String direction) {
        return service.getAll(page, size, sort, direction).map(mapper::toDTO);
    }

    @Override
    public TaskResponse update(Long id, TaskRequest taskRequest) {
        validateRequest(taskRequest);
        Task task = service.getById(id);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        updateTaskAttributes(task, taskRequest);
        return mapper.toDTO(service.save(task));
    }

    @Override
    public TaskResponse updateStatus(Long id, String status) {
        Task task = service.getById(id);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        validateStatus(status);

        task.setStatus(TaskStatus.valueOf(status));
        return mapper.toDTO(service.save(task));
    }

    @Override
    public void delete(Long id) {
        Task task = service.getById(id);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        service.delete(id);
        deleteCache(id);
    }

    private void validateStatus(String status) {
        try {
            TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        }
    }

    @Override
    public TaskResponse create(TaskRequest taskRequest) {
        validateRequest(taskRequest);
        Task task = mapper.toEntity(taskRequest);
        return mapper.toDTO(service.save(task));
    }

    private void validateRequest(TaskRequest taskRequest) {
        if (LocalDateTime.parse(taskRequest.getDueDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.DUE_DATE_MUST_BE_GREATER_THAN_CURRENT_DATE);
        }
    }

    private void updateTaskAttributes(Task task, TaskRequest taskRequest) {
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(LocalDateTime.parse(taskRequest.getDueDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        task.setPriority(taskRequest.getPriority());
    }

    private void deleteCache(Long taskId) {
        localCache.invalidate(taskId);
        redisCache.delete("TASK:" + taskId);
    }
}
