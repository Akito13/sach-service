package com.bookshop.sachservice.controller;

import com.bookshop.sachservice.dto.LoaiByGroupParentDto;
import com.bookshop.sachservice.dto.ResponsePayload;
import com.bookshop.sachservice.service.impl.LoaiGroupServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "api/loai", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class LoaiController {

    private LoaiGroupServiceImpl loaiGroupService;

    @GetMapping("all")
    public ResponseEntity getAllLoaiByParent() {
        Set<LoaiByGroupParentDto> loaiByGroupParentDtoSet = loaiGroupService.findAll();
        return new ResponseEntity(loaiByGroupParentDtoSet, HttpStatus.OK);
    }
}
