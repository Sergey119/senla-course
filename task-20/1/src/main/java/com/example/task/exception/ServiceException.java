package com.example.task.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServiceException() {
        super();
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}