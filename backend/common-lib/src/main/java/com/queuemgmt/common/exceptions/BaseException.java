package com.queuemgmt.common.exceptions;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/* Base Exception for all custom exceptions across the microservices */
@Getter
public class BaseException extends RuntimeException{
    
    private final HttpStatusCode statusCode;
    private final String errorCode;

    public BaseException(String message, HttpstatusCode statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = statusCode.getName();
    }

    public BaseException(String message, HttpStatusCode statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
}
