package com.tainn.todo.application.service.impl;

import com.tainn.todo.application.service.TaskDependencyAppService;
import com.tainn.todo.application.service.cache.TaskCacheAppService;
import com.tainn.todo.domain.model.dto.response.TaskResponse;
import com.tainn.todo.domain.model.entity.TaskDependency;
import com.tainn.todo.domain.model.exception.BusinessException;
import com.tainn.todo.domain.model.exception.ErrorCode;
import com.tainn.todo.domain.service.TaskDependencyService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TaskDependencyAppServiceImpl implements TaskDependencyAppService {
    TaskDependencyService taskDependencyService;
    TaskCacheAppService taskCacheService;

    @Override
    public List<TaskResponse> addDependency(Long taskId, Long dependencyId) {
        boolean isCircularDependency = taskDependencyService.hasCircularDependency(taskId, dependencyId);
        if (isCircularDependency) {
            throw new BusinessException(ErrorCode.CIRCULAR_DEPENDENCY);
        }
        TaskDependency taskDependency = taskDependencyService.addDependency(taskId, dependencyId);
        return viewAllDependencies(taskDependency.getTask().getId());
    }

    @Override
    public List<TaskResponse> removeDependency(Long taskId, Long dependencyId) {
        taskDependencyService.removeDependency(taskId, dependencyId);
        return viewAllDependencies(taskId);
    }

    @Override
    public List<TaskResponse> viewAllDependencies(Long taskId) {
        return taskDependencyService.getAllDependencies(taskId).stream()
                .map(taskDependency -> {
                    TaskResponse response = taskCacheService.getTask(taskDependency.getTaskId());
                    response.setDependencies(viewAllDependencies(taskDependency.getTaskId()));
                    return response;
                })
                .collect(Collectors.toList());
    }
}
