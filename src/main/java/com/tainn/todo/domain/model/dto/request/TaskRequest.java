package com.tainn.todo.domain.model.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class TaskRequest {
    @NotBlank(message = "TITLE_IS_REQUIRED")
    String title;

    @Lob
    @NotBlank(message = "DESCRIPTION_IS_REQUIRED")
    String description;

    @NotBlank(message = "DUE_DATE_IS_REQUIRED")
    @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "INVALID_DATE_FORMAT")
    String dueDate;

    @Min(value = 1, message = "PRIORITY_MUST_BE_GREATER_THAN_ZERO")
    int priority;
}
