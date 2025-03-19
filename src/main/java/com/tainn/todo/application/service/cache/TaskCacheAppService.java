package com.tainn.todo.application.service.cache;

import com.tainn.todo.domain.model.dto.response.TaskResponse;

public interface TaskCacheAppService {
    TaskResponse getTask(Long taskId);
}
