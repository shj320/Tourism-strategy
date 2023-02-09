package com.sj.exception;

/**
 * 自定义异常给用户看的
 */
public class LogicException extends RuntimeException{

    public LogicException(String message) {
        super(message);
    }
}
