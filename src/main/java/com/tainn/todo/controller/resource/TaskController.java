package com.tainn.todo.controller.resource;

import com.tainn.todo.application.service.TaskAppService;
import com.tainn.todo.application.service.TaskDependencyAppService;
import com.tainn.todo.controller.model.builder.ResponseFactory;
import com.tainn.todo.controller.model.vo.ApiResponse;
import com.tainn.todo.domain.model.dto.request.TaskRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    ResponseFactory responseFactory;
    TaskAppService taskAppService;
    TaskDependencyAppService taskDependencyAppService;

    @GetMapping
    public ApiResponse getTasks(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "10") int size,
                                @RequestParam(required = false, defaultValue = "id") String sortBy,
                                @RequestParam(required = false, defaultValue = "asc") String direction,
                                @RequestParam(required = false) String status,
                                @RequestParam(required = false) String title
    ) {
        return responseFactory.create(taskAppService.getAllWithFilter(page, size, sortBy, direction, status, title));
    }

    @GetMapping("/{id}")
    public ApiResponse getTask(@PathVariable Long id) {
        return responseFactory.create(taskAppService.getById(id));
    }

    @PostMapping
    public ApiResponse createTask(@RequestBody @Valid TaskRequest taskRequest) {
        return responseFactory.create(taskAppService.create(taskRequest));
    }

    @PutMapping("/{id}")
    public ApiResponse updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest taskRequest) {
        return responseFactory.create(taskAppService.update(id, taskRequest));
    }

    @PatchMapping("/{id}")
    public ApiResponse updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        return responseFactory.create(taskAppService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteTask(@PathVariable Long id) {
        taskAppService.delete(id);
        return responseFactory.create(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{taskId}/dependencies")
    public ApiResponse addDependency(@PathVariable Long taskId, @RequestParam Long dependencyId) {
        return responseFactory.create(taskDependencyAppService.addDependency(taskId, dependencyId));
    }

    @DeleteMapping("/{taskId}/dependencies")
    public ApiResponse removeDependency(@PathVariable Long taskId, @RequestParam Long dependencyId) {
        return responseFactory.create(taskDependencyAppService.removeDependency(taskId, dependencyId));
    }

    @GetMapping("/{taskId}/dependencies")
    public ApiResponse viewAllDependencies(@PathVariable Long taskId) {
        return responseFactory.create(taskDependencyAppService.viewAllDependencies(taskId));
    }
}
