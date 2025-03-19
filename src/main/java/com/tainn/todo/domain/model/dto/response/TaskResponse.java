package com.tainn.todo.domain.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tainn.todo.domain.model.vo.TaskStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class TaskResponse {
    Long id;
    String title;
    String description;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    LocalDateTime dueDate;
    int priority;
    TaskStatus status;
    List<TaskResponse> dependencies;
    Long version;

}
