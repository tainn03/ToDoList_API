package com.tainn.todo.domain.model.mapper;

import com.tainn.todo.domain.model.dto.request.TaskRequest;
import com.tainn.todo.domain.model.dto.response.TaskResponse;
import com.tainn.todo.domain.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "dependencies", ignore = true)
    TaskResponse toDTO(Task task);

    @Mapping(target = "dueDate", source = "dueDate", dateFormat = "yyyy/MM/dd HH:mm:ss")
    Task toEntity(TaskRequest request);
}