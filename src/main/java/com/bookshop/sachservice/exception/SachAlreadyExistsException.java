package com.bookshop.sachservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SachAlreadyExistsException extends RuntimeException{
    public SachAlreadyExistsException(String message){
        super(message);
    }
}
