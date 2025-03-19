package com.tainn.todo.domain.service;

import com.tainn.todo.domain.model.dto.response.TaskDependencyResponse;
import com.tainn.todo.domain.model.entity.TaskDependency;

import java.util.List;

public interface TaskDependencyService extends BaseService<TaskDependency, Long> {
    List<TaskDependencyResponse> getAllDependencies(Long taskId);

    TaskDependency addDependency(Long taskId, Long dependencyId);

    void removeDependency(Long taskId, Long dependencyId);

    boolean hasCircularDependency(Long taskId, Long dependencyId);
}
