package com.tainn.todo.domain.model.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // code convention: http status code + sequence number
    SUCCESS(20000, "Success", HttpStatus.OK),

    UNCATEGORIZED(50001, "System error, please check the log", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_RESOURCE_FOUND(40401, "API address not found", HttpStatus.NOT_FOUND),
    TASK_NOT_FOUND(40402, "Task not found", HttpStatus.NOT_FOUND),
    TASK_DEPENDENCY_NOT_FOUND(40403, "Task dependency not found", HttpStatus.NOT_FOUND),

    INVALID_KEY(40001, "Unknown error", HttpStatus.BAD_REQUEST),
    TITLE_IS_REQUIRED(40002, "Title must not be empty", HttpStatus.BAD_REQUEST),
    DESCRIPTION_IS_REQUIRED(40002, "Description must not be empty", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT(40002, "Date format must be yyyy/MM/dd HH:mm:ss", HttpStatus.BAD_REQUEST),
    DUE_DATE_IS_REQUIRED(40003, "Due date must not be empty", HttpStatus.BAD_REQUEST),
    PRIORITY_MUST_BE_GREATER_THAN_ZERO(40003, "Priority must be greater than 0", HttpStatus.BAD_REQUEST),
    DUE_DATE_MUST_BE_GREATER_THAN_CURRENT_DATE(40003, "Due date must be greater than the current date", HttpStatus.BAD_REQUEST),
    INVALID_STATUS(40004, "Invalid status", HttpStatus.BAD_REQUEST),
    CIRCULAR_DEPENDENCY(40005, "Circular dependency detected", HttpStatus.BAD_REQUEST),
    ;
    int code;
    String message;
    HttpStatusCode statusCode;
}
