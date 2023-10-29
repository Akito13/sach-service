package com.bookshop.sachservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponseDto {
    private HttpStatus statusCode;
    private String message;
    private LocalDateTime timestamp;
    private String apiPath;
    private Map<String, String> errors;
}
