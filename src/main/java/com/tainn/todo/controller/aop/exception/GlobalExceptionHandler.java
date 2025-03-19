package com.tainn.todo.controller.aop.exception;

import com.tainn.todo.controller.model.vo.ApiResponse;
import com.tainn.todo.domain.model.exception.BusinessException;
import com.tainn.todo.domain.model.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> handleRunTimeException(Exception e) {
        ApiResponse response = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED.getStatusCode()).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {
        String enumKey = e.getErrorCode().name();
        return getResponseEntity(enumKey);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return getResponseEntity(enumKey);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ApiResponse> handleNoResourceFoundException(Exception e) {
        String enumKey = ErrorCode.NO_RESOURCE_FOUND.name();
        return getResponseEntity(enumKey);
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<ApiResponse> handleDateTimeParseException(DateTimeParseException e) {
        String enumKey = ErrorCode.INVALID_DATE_FORMAT.name();
        return getResponseEntity(enumKey);
    }

    private ResponseEntity<ApiResponse> getResponseEntity(String enumKey) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        ApiResponse response;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ignored) {
        }
        response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}
