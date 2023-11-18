package com.bookshop.sachservice.mapper;

import com.bookshop.sachservice.dto.*;
import com.bookshop.sachservice.model.GiaSach;
import com.bookshop.sachservice.model.Loai;
import com.bookshop.sachservice.model.Sach;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class CommonMapper {
    public static SachDto mapToSachDto(Sach sach, Loai loai, GiaSach giaSach){
        return SachDto.builder()
                .id(sach.getId())
                .moTa(sach.getMoTa() == null ? "Chưa có mô tả" : sach.getMoTa())
                .tacGia(sach.getTacGia())
                .nxb(sach.getNxb())
                .ten(sach.getTen())
                .trangThai(sach.getTrangThai())
                .anh(sach.getAnh())
                .soLuong(sach.getSoLuong())
                .loaiDto(mapToLoaiDto(loai))
                .giaSach(mapToGiaDto(giaSach))
                .build();
    }

    public static Sach mapToSach(SachDto sachDto, LoaiDto loaiDto, GiaDto giaDto){
        return Sach.builder()
                .moTa(sachDto.getMoTa())
                .tacGia(sachDto.getTacGia())
                .nxb(sachDto.getNxb())
                .ten(sachDto.getTen())
                .trangThai(sachDto.getTrangThai())
                .anh(sachDto.getAnh())
                .soLuong((sachDto.getSoLuong() == null || sachDto.getSoLuong() < 0) ? 0 : sachDto.getSoLuong())
//                .loai(new ObjectId(loaiDto.getMa()))
                .loai(mapToLoai(loaiDto))
                .giaSach(mapToGiaSach(giaDto))
                .build();
    }

    public static Loai mapToLoai(LoaiDto loaiDto){
        return Loai.builder()
                .ma(loaiDto.getMa())
//                .id(new ObjectId(loaiDto.getMa()))
                .ten(loaiDto.getTen())
                .parent(loaiDto.getParent())
                .build();
    }

    public static LoaiDto mapToLoaiDto(Loai loai){
        return LoaiDto.builder()
                .ma(loai.getMa())
                .ten(loai.getTen())
                .parent(loai.getParent())
                .build();
    }

    public static GiaDto mapToGiaDto(GiaSach giaSach){
        return GiaDto.builder()
                .giaBan(giaSach.getGiaBan())
                .giaGoc(giaSach.getGiaGoc())
                .thoiGian(new TimeRange(giaSach.getStartTime(), giaSach.getEndTime()))
                .phanTramGiam(giaSach.getPhanTramGiam())
                .build();
    }

    public static GiaSach mapToGiaSach(GiaDto giaDto){
        return GiaSach.builder()
                .giaBan(giaDto.getGiaBan())
                .giaGoc(giaDto.getGiaGoc())
                .startTime(giaDto.getThoiGian() == null ? null : giaDto.getThoiGian().getStartTime())
                .endTime(giaDto.getThoiGian() == null ? null : giaDto.getThoiGian().getEndTime())
                .phanTramGiam(giaDto.getPhanTramGiam() == null ? 0 : giaDto.getPhanTramGiam())
                .build();
    }

    public static ErrorResponseDto buildErrorResponse(RuntimeException exception, WebRequest request, Map<String, String> errors){
        return ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST)
                .errors(errors)
                .build();
    }
}
