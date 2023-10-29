package com.bookshop.sachservice.exception;

public class InvalidBodyException extends RuntimeException{
    public InvalidBodyException(String message) {
        super(message);
    }
}
