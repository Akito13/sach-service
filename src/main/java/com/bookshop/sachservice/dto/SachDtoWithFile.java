package com.bookshop.sachservice.dto;

import com.bookshop.sachservice.validation.ValidateDateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SachDtoWithFile {
    private Long id;
    @NotBlank(message = "Tên không bỏ trống")
    private String ten;
    private String moTa;
    @NotNull(message = "Ngày xuất bản không bỏ trống")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nxb;
    @NotBlank(message = "Tác giả không bỏ trống")
    private String tacGia;
    @NotBlank(message = "Ảnh không bỏ trống")
    private String anh;
    private Boolean trangThai;
    private Integer soLuong;

    @NotNull(message = "Mã loại chưa xác định")
    private String maLoai;
    @NotNull(message = "Tên loại xác định")
    private String tenLoai;
    @NotNull(message = "Loại cha chưa xác định")
    private String parentLoai;

    @PositiveOrZero(message = "Giá gốc không thể âm")
    private BigDecimal giaGoc;

    @Positive(message = "Giá bán không thể nhỏ hơn 0")
    private BigDecimal giaBan;

//    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @Min(value = 0, message = "Phần trăm giảm phải hơn 0")
    @Max(value = 1, message = "Phần trăm giảm phải nhỏ hơn 1")
    private Double phanTramGiam;

    private MultipartFile img;
}
