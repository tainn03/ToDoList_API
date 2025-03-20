package com.tainn.todo.domain.service.impl;

import com.tainn.todo.domain.model.entity.Task;
import com.tainn.todo.domain.model.entity.TaskDependency;
import com.tainn.todo.domain.model.exception.BusinessException;
import com.tainn.todo.domain.model.exception.ErrorCode;
import com.tainn.todo.domain.model.vo.TaskStatus;
import com.tainn.todo.domain.repository.TaskRepository;
import com.tainn.todo.domain.service.TaskService;
import com.tainn.todo.domain.utils.TaskSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {
    TaskRepository repository;

    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public Task update(Task task) {
        return repository.save(task);
    }

    @Override
    public Task getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Task> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<Task> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return repository.findAll(pageable);
    }

    @Override
    public Page<Task> getAllWithFilter(int page, int size, String sort, String direction, TaskStatus status, String title) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Specification<Task> spec = Specification.where(null);
        if (status != null) {
            spec = spec.and(TaskSpecification.hasStatus(status));
        }
        if (title != null && !title.isEmpty()) {
            spec = spec.and(TaskSpecification.hasTitle(title));
        }
        return repository.findAll(spec, pageable);
    }

    @Override
    public Task updateStatus(Long id, TaskStatus taskStatus) {
        Task task = getById(id);
        checkStatusDependencies(task, taskStatus);
        task.setStatus(taskStatus);
        return repository.save(task);
    }

    private void checkStatusDependencies(Task task, TaskStatus taskStatus) {
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        if (taskStatus == TaskStatus.COMPLETED) {
            for (TaskDependency dependency : task.getDependencies()) {
                if (dependency.getDependsOnTask().getStatus() != TaskStatus.COMPLETED) {
                    throw new BusinessException(ErrorCode.DEPENDENCIES_NOT_COMPLETED);
                }
            }
        }
    }
}
