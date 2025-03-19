package com.tainn.todo.domain.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class TaskDependencyResponse {
    long taskId;
    List<TaskDependencyResponse> dependsOnTasks;
}
