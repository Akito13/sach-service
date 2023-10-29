package com.bookshop.sachservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoaiDto {
    @NotBlank(message = "Mã loại chưa có")
    private String ma;
    @NotBlank(message = "Tên loại chưa có")
    private String ten;
    @NotBlank(message = "Loại gốc chưa có")
    private String parent;
}
