package com.bookshop.sachservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrangThaiSach {
    private int sachId;
    private BigDecimal gia;
    private Boolean trangThai;
}
