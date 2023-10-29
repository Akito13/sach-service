package com.bookshop.sachservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter @Setter
public class TimeRange {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
