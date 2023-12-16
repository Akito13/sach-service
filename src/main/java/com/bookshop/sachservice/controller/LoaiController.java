package com.bookshop.sachservice.controller;

import com.bookshop.sachservice.dto.LoaiByGroupParentDto;
import com.bookshop.sachservice.dto.LoaiDto;
import com.bookshop.sachservice.dto.ResponseDto;
import com.bookshop.sachservice.dto.ResponsePayload;
import com.bookshop.sachservice.service.impl.LoaiGroupServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "api/loai", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class LoaiController {

    private LoaiGroupServiceImpl loaiGroupService;

    @PostMapping("new")
    public ResponseEntity createLoai(@RequestBody LoaiDto loaiDto) {
        loaiGroupService.create(loaiDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("parents")
    public ResponseEntity<Set<String>> getAllParents() {
        Set<String> parents = loaiGroupService.findParents();
        return new ResponseEntity<>(parents, HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity getAllLoaiByParent() {
        Set<LoaiByGroupParentDto> loaiByGroupParentDtoSet = loaiGroupService.findAll();
        return new ResponseEntity(loaiByGroupParentDtoSet, HttpStatus.OK);
    }

    @GetMapping("allNonGrouping")
    public ResponseEntity<ResponseDto<LoaiDto>> getAllLoaiNonGrouping(WebRequest request) {
        List<LoaiDto> loaiDtos = loaiGroupService.findAllWithougGrouping();
        ResponsePayload<LoaiDto> payload = ResponsePayload.<LoaiDto>builder()
                .totalPages(1).pageSize(loaiDtos.size()).currentPageSize(loaiDtos.size()).currentPage(0).recordCounts((long)loaiDtos.size())
                .records(loaiDtos)
                .build();
        ResponseDto<LoaiDto> res = ResponseDto.<LoaiDto>builder()
                .apiPath(request.getDescription(false))
                .message("OK")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK)
                .payload(payload)
                .build();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
