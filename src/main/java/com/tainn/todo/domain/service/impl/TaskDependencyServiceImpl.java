package com.tainn.todo.domain.service.impl;

import com.tainn.todo.domain.model.dto.response.TaskDependencyResponse;
import com.tainn.todo.domain.model.entity.Task;
import com.tainn.todo.domain.model.entity.TaskDependency;
import com.tainn.todo.domain.model.exception.BusinessException;
import com.tainn.todo.domain.model.exception.ErrorCode;
import com.tainn.todo.domain.model.mapper.TaskMapper;
import com.tainn.todo.domain.repository.TaskDependencyRepository;
import com.tainn.todo.domain.repository.TaskRepository;
import com.tainn.todo.domain.service.TaskDependencyService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TaskDependencyServiceImpl implements TaskDependencyService {
    TaskDependencyRepository repository;
    TaskRepository taskRepository;
    TaskMapper taskMapper;

    @Override
    public TaskDependency save(TaskDependency taskDependency) {
        return repository.save(taskDependency);
    }

    @Override
    public TaskDependency update(TaskDependency taskDependency) {
        return repository.save(taskDependency);
    }

    @Override
    public TaskDependency getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<TaskDependency> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<TaskDependency> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return repository.findAll(pageable);
    }

    @Override
    public List<TaskDependencyResponse> getAllDependencies(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));
        TaskDependencyResponse taskDependencyResponse = new TaskDependencyResponse();
        taskDependencyResponse.setTaskId(taskId);
        return task.getDependencies().stream()
                .map(dependency -> {
                    TaskDependencyResponse response = new TaskDependencyResponse();
                    response.setTaskId(dependency.getDependsOnTask().getId());
                    response.setDependsOnTasks(getAllDependencies(dependency.getDependsOnTask().getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TaskDependency addDependency(Long taskId, Long dependencyId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));
        Task dependsOnTask = taskRepository.findById(dependencyId).orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));
        TaskDependency taskDependency = TaskDependency.builder()
                .task(task)
                .dependsOnTask(dependsOnTask)
                .build();
        return save(taskDependency);
    }

    @Override
    public void removeDependency(Long taskId, Long dependencyId) {
        TaskDependency taskDependency = repository.findByTaskIdAndDependsOnTaskId(taskId, dependencyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_DEPENDENCY_NOT_FOUND));
        delete(taskDependency.getId());
    }

    @Override
    public boolean hasCircularDependency(Long taskId, Long dependencyId) {
        // Xây dựng danh sách kề (Adjacency List)
        Map<Long, List<Long>> adjacencyList = new HashMap<>();
        for (TaskDependency dependency : getAll()) {
            adjacencyList
                    .computeIfAbsent(dependency.getTask().getId(), k -> new ArrayList<>())
                    .add(dependency.getDependsOnTask().getId());
        }

        // Thêm phụ thuộc mới vào danh sách kề
        adjacencyList
                .computeIfAbsent(taskId, k -> new ArrayList<>())
                .add(dependencyId);

        // Trạng thái duyệt của node (0 = chưa thăm, 1 = đang duyệt, 2 = đã duyệt)
        Map<Long, Integer> status = new HashMap<>();
        for (Long taskKey : adjacencyList.keySet()) {
            status.put(taskKey, 0);
        }

        // Danh sách để lưu trữ đường đi
        List<Long> path = new ArrayList<>();

        // Kiểm tra chu trình bằng DFS
        return detectCycle(taskId, adjacencyList, status, path);
    }

    // DFS để phát hiện vòng lặp
    private boolean detectCycle(Long task, Map<Long, List<Long>> adjacencyList, Map<Long, Integer> status, List<Long> path) {
        status.put(task, 1); // Đánh dấu là đang duyệt (GRAY)
        path.add(task); // Thêm task vào đường đi

        for (Long dependency : adjacencyList.getOrDefault(task, Collections.emptyList())) {
            if (status.getOrDefault(dependency, 0) == 1) { // Nếu node đang duyệt, có vòng lặp
                path.add(dependency); // Thêm node gây vòng lặp vào đường đi
                logCycle(path, dependency); // Log đường đi
                return true;
            }
            if (status.getOrDefault(dependency, 0) == 0) { // Nếu chưa duyệt, tiếp tục DFS
                if (detectCycle(dependency, adjacencyList, status, path)) {
                    return true;
                }
            }
        }

        status.put(task, 2); // Đánh dấu là đã duyệt xong (BLACK)
        path.remove(task); // Loại bỏ task khỏi đường đi
        return false;
    }

    // Log đường đi của vòng lặp
    private void logCycle(List<Long> path, Long start) {
        StringBuilder cycle = new StringBuilder();
        for (Long node : path) {
            cycle.append(node).append(" -> ");
        }
        cycle.delete(cycle.length() - 4, cycle.length());
        System.out.println("Circular dependency detected: " + cycle);
    }
}
