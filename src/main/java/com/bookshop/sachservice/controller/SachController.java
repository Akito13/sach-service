package com.bookshop.sachservice.controller;

import com.bookshop.sachservice.constants.SachConstants;
import com.bookshop.sachservice.dto.ResponseDto;
import com.bookshop.sachservice.dto.ResponsePayload;
import com.bookshop.sachservice.dto.SachDto;
import com.bookshop.sachservice.dto.TrangThaiSach;
import com.bookshop.sachservice.model.Sach;
import com.bookshop.sachservice.service.impl.SachServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/sach", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class SachController {

    private SachServiceImpl crudService;

    @PostMapping
    public ResponseEntity<ResponseDto<SachDto>> createSach(@Valid @RequestBody SachDto sachDto, WebRequest request) {
        List<SachDto> createdSachDtos = crudService.create(sachDto);
        ResponsePayload<SachDto> payload = ResponsePayload.<SachDto>
                        builder()
                .records(createdSachDtos).recordCounts((long) createdSachDtos.size())
                .currentPage(0).currentPageSize(1).totalPages(1).pageSize(1)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.<SachDto>builder()
                        .apiPath(request.getDescription(false))
                        .message(SachConstants.MESSAGE_CREATED)
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED)
                        .payload(payload)
                        .build()
                );
    }

    @GetMapping("{role}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseDto<SachDto>> getAllProducts(@RequestParam("page") Optional<Integer> os,
                                                               @RequestParam("pageSize") Optional<Integer> ps,
                                                               @RequestParam("ten") Optional<String> ts,
                                                               @RequestParam("loai") Optional<String> tl,
                                                               @RequestParam("gia") Optional<BigDecimal> g,
                                                               @PathVariable("role") String role,
                                                               WebRequest request) {
        int page = os.orElse(0);
        int pageSize = ps.orElse(3);
        String tenSach = ts.orElse("");
        String tenLoai = tl.orElse("");
        BigDecimal gia = g.orElse(BigDecimal.valueOf(Integer.MAX_VALUE));
        Page<SachDto> sachDtoPage = null;
        if (role.equals("admin")) {
            sachDtoPage = crudService.findWithConditionAdmin(page, pageSize, tenSach, tenLoai, gia);
        } else {
            sachDtoPage = crudService.findWithConditionUser(page, pageSize, tenSach, tenLoai, gia);
        }

        List<SachDto> foundSachDtos = sachDtoPage.getContent();
        System.out.println(foundSachDtos);
        System.out.println(foundSachDtos.size());
        ResponsePayload<SachDto> payload = ResponsePayload.<SachDto>builder()
                .records(foundSachDtos)
                .recordCounts(sachDtoPage.getTotalElements())
                .currentPage(sachDtoPage.getNumber())
                .currentPageSize(sachDtoPage.getNumberOfElements())
                .totalPages(sachDtoPage.getTotalPages())
                .pageSize(sachDtoPage.getSize())
                .build();

        System.out.println("Successfully get Sach");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.<SachDto>builder()
                        .apiPath(request.getDescription(false))
                        .message(SachConstants.MESSAGE_OK)
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK)
                        .payload(payload)
                        .build()
                );
    }

    @PutMapping
    public ResponseEntity<ResponseDto<SachDto>> updateSach(@Valid @RequestBody SachDto sachDto, WebRequest request) {
        List<SachDto> createdSachDtos = crudService.update(sachDto, sachDto.getTrangThai());
        ResponsePayload<SachDto> payload = ResponsePayload.<SachDto>
                        builder()
                .records(createdSachDtos).recordCounts((long) createdSachDtos.size())
                .currentPage(0).currentPageSize(1).totalPages(1).pageSize(1)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.<SachDto>builder()
                        .apiPath(request.getDescription(false))
                        .message(SachConstants.MESSAGE_OK)
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK)
                        .payload(payload)
                        .build()
                );
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<SachDto>> markSachAsDeleted(@RequestBody SachDto sachDto, WebRequest request) {
        crudService.delete(sachDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.<SachDto>builder()
                        .apiPath(request.getDescription(false))
                        .message(SachConstants.MESSAGE_OK)
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK)
                        .payload(null)
                        .build()
                );
    }

//    Internal API
    @GetMapping("trangThaiGia")
    public List<TrangThaiSach> getTrangThaiGia(@RequestParam List<Integer> sachIds) {
        return crudService.getTrangThaiGia(sachIds)
                .stream()
                .sorted(Comparator.comparingInt(Sach::getId))
                .map(sach -> new TrangThaiSach(sach.getId(), sach.getGiaSach().getGiaBan(), sach.getTrangThai()))
                .toList();
    }
    
    @GetMapping("id")
    public Integer getIdSach(@RequestParam Integer sachId){
        return crudService.getIdSach(sachId);
    }
}