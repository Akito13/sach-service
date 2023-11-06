package com.bookshop.sachservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SachDto {
    private Long id;
    @NotBlank(message = "Tên không bỏ trống")
    private String ten;
    private String moTa;
    @NotBlank(message = "Ngày xuất bản không bỏ trống")
    private String nxb;
    @NotBlank(message = "Tác giả không bỏ trống")
    private String tacGia;
    @NotBlank(message = "Ảnh không bỏ trống")
    private String anh;
    private Boolean trangThai;
    private Integer soLuong;

    @NotNull(message = "Loại chưa xác định")
    @Valid
    private LoaiDto loaiDto;

    @Valid
    @NotNull(message = "Giá chưa xác định")
    private GiaDto giaSach;
}
