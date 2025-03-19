package com.tainn.todo.domain.model.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
