package com.bookshop.sachservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseDto<T> {
    private HttpStatus statusCode;
    private String message;
    private LocalDateTime timestamp;
    private String apiPath;
    private ResponsePayload<T> payload;
}
