package com.bookshop.sachservice.exception;

public class LoaiAlreadyExistException extends RuntimeException{
    public LoaiAlreadyExistException(String message) {
        super(message);
    }
}
