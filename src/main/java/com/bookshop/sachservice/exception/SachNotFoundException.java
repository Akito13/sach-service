package com.bookshop.sachservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SachNotFoundException extends RuntimeException{
    public SachNotFoundException(String message) {
        super(message);
    }
}
