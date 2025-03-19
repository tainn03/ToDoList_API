package com.tainn.todo.controller.model.builder;

import com.tainn.todo.controller.model.vo.ApiResponse;
import com.tainn.todo.domain.model.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public interface ResponseFactory {
    ApiResponse create(HttpStatus status, Object data);

    ApiResponse create(Object data);

    ApiResponse create(ErrorCode errorCode);
}