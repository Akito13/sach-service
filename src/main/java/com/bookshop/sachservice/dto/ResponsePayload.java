package com.bookshop.sachservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResponsePayload<T> {
    private Long recordCounts;
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Integer currentPageSize;
    private List<T> records;
}
