package com.tainn.todo.domain.service;

import com.tainn.todo.domain.model.entity.Task;
import com.tainn.todo.domain.model.vo.TaskStatus;
import org.springframework.data.domain.Page;

public interface TaskService extends BaseService<Task, Long> {
    Task updateStatus(Long id, TaskStatus taskStatus);

    Page<Task> getAllWithFilter(int page, int size, String sort, String direction, TaskStatus status, String title);
}
