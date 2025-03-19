package com.tainn.todo.application.service;

import com.tainn.todo.domain.model.dto.response.TaskResponse;

import java.util.List;

public interface TaskDependencyAppService {
    List<TaskResponse> addDependency(Long taskId, Long dependencyId);

    List<TaskResponse> removeDependency(Long taskId, Long dependencyId);

    List<TaskResponse> viewAllDependencies(Long taskId);
}
