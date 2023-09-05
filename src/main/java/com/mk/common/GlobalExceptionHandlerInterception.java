package com.mk.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

// 全局异常处理器（拦截器）
@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandlerInterception {

    // SQL Employee Name 唯一约束拦截
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R sqlPrimaryKeyExceptionHandler(Exception exception){
        exception.printStackTrace();
        String[] split = exception.getMessage().split(":");
        return R.ERROR(split[2].substring(0, (split[2].indexOf("#"))));
    }

    // 业务层异常捕获
    @ExceptionHandler(CustomServiceException.class)
    public R serviceException(Exception exception){
        log.info("捕获自定义业务异常：{}", exception.getMessage());
        exception.printStackTrace();
        return R.ERROR(exception.getMessage());
    }

}
