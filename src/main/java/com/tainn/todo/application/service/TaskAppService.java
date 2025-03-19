package com.tainn.todo.application.service;

import com.tainn.todo.domain.model.dto.request.TaskRequest;
import com.tainn.todo.domain.model.dto.response.TaskResponse;
import org.springframework.data.domain.Page;

public interface TaskAppService {
    TaskResponse getById(Long id);

    TaskResponse create(TaskRequest taskRequest);

    Page<TaskResponse> getAll(int page, int size, String sortBy, String direction);

    TaskResponse update(Long id, TaskRequest taskRequest);

    TaskResponse updateStatus(Long id, String status);

    void delete(Long id);
}
