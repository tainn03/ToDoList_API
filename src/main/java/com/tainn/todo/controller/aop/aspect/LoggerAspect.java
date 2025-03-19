package com.tainn.todo.controller.aop.aspect;

import com.tainn.todo.controller.model.vo.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggerAspect {
    @Around("execution(* com.tainn.todo.controller.resource..*.*(..))")
    public Object logAroundApiController(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long time = end.toEpochMilli() - start.toEpochMilli();

        logExecutionDetails(joinPoint, result, time);

        return result;
    }

    private void logExecutionDetails(ProceedingJoinPoint joinPoint, Object result, long time) {
        log.info("FINISH EXECUTION METHOD {} RUN IN {} ms", joinPoint.getSignature().getName(), time);
        if (result instanceof ApiResponse apiResponse) {
            apiResponse.setExecutionTime(time);
        } else if (result instanceof ResponseEntity<?> responseEntity && responseEntity.getBody() instanceof ApiResponse apiResponse) {
            apiResponse.setExecutionTime(time);
        }
    }

    @AfterThrowing(pointcut = "execution(* com.tainn.todo.controller..*.*(..))", throwing = "ex")
    public void logAfterThrowingApiController(Exception ex) {
        log.error("FAIL METHOD {} DUE TO EXCEPTION: {}", ex.getStackTrace()[0].getMethodName(), ex.getMessage());
    }
}