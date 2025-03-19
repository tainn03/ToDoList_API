package com.tainn.todo.controller.resource;

import com.tainn.todo.application.service.TaskAppService;
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

    @GetMapping
    public ApiResponse getTasks(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "10") int size,
                                @RequestParam(required = false, defaultValue = "id") String sortBy,
                                @RequestParam(required = false, defaultValue = "asc") String direction) {
        return responseFactory.create(taskAppService.getAll(page, size, sortBy, direction));
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
}
