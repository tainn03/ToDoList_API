package com.tainn.todo.controller.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tainn.todo.domain.model.exception.ErrorCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    @Builder.Default
    int code = ErrorCode.SUCCESS.getCode();

    @Builder.Default
    String message = ErrorCode.SUCCESS.getMessage();

    @Builder.Default
    Long executionTime = 0L;

    Object payload;
}
