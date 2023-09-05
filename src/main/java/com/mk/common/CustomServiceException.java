package com.mk.common;

// 自定义业务异常
public class CustomServiceException extends RuntimeException {
    public CustomServiceException(String message) {
        super(message);
    }
}
