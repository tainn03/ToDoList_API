package com.tainn.todo.domain.service.impl;

import com.tainn.todo.domain.model.entity.Task;
import com.tainn.todo.domain.repository.TaskRepository;
import com.tainn.todo.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
}
