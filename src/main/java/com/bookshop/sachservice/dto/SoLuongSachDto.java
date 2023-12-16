package com.bookshop.sachservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoLuongSachDto implements Comparable<Long>{
    private Long sachId;
    private String tenSach;
    private Integer soLuong;
    private Boolean trangThai;
    private Double phanTramGiam;
    private BigDecimal donGia;

    @Override
    public int compareTo(Long o) {
        return this.sachId.compareTo(o);
    }
}