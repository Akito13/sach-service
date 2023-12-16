package com.bookshop.sachservice.dto;

import com.bookshop.sachservice.validation.ValidateDateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiaDto {

    @PositiveOrZero(message = "Giá gốc không thể âm")
//    @NotNull(message = "Giá gốc chưa có")
    private BigDecimal giaGoc;

    @Positive(message = "Giá bán không thể nhỏ hơn 0")
    private BigDecimal giaBan;

    @ValidateDateRange
    private TimeRange thoiGian;

    @Min(value = 0, message = "Phần trăm giảm phải hơn 0")
    @Max(value = 1, message = "Phần trăm giảm phải nhỏ hơn 1")
    private Double phanTramGiam;
}
