package com.tainn.todo.controller.resource;

import com.tainn.todo.application.service.TaskDependencyAppService;
import com.tainn.todo.controller.model.builder.ResponseFactory;
import com.tainn.todo.controller.model.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dependencies")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TaskDependencyController {
    ResponseFactory responseFactory;
    TaskDependencyAppService taskDependencyAppService;

    @PostMapping("/add")
    public ApiResponse addDependency(@RequestParam Long taskId, @RequestParam Long dependencyId) {
        return responseFactory.create(taskDependencyAppService.addDependency(taskId, dependencyId));
    }

    @DeleteMapping("/remove")
    public ApiResponse removeDependency(@RequestParam Long taskId, @RequestParam Long dependencyId) {
        return responseFactory.create(taskDependencyAppService.removeDependency(taskId, dependencyId));
    }

    @GetMapping("{taskId}")
    public ApiResponse viewAllDependencies(@PathVariable Long taskId) {
        return responseFactory.create(taskDependencyAppService.viewAllDependencies(taskId));
    }
}
