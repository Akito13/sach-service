package com.bookshop.sachservice.exception;

import com.bookshop.sachservice.dto.ErrorResponseDto;
import com.bookshop.sachservice.dto.ResponseDto;
import com.bookshop.sachservice.dto.SachDto;
import com.bookshop.sachservice.mapper.CommonMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SachAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleSachAlreadyExistsException(SachAlreadyExistsException exception, WebRequest request){
        return new ResponseEntity<>(CommonMapper.buildErrorResponse(exception, request, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SachNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleSachNotFoundException(SachNotFoundException exception, WebRequest request){
            return new ResponseEntity<>(CommonMapper.buildErrorResponse(exception, request, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBodyException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidBodyException(InvalidBodyException exception, WebRequest request){
        return new ResponseEntity<>(CommonMapper.buildErrorResponse(exception, request, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .message("Thông tin nhập không hợp lệ")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST)
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<SachDto>> handleGlobalException(Exception exception, WebRequest request){
        ResponseDto<SachDto> responseDto = ResponseDto.<SachDto>builder()
                .apiPath(request.getDescription(true))
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .payload(null)
                .build();
        exception.printStackTrace();
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
