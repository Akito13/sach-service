package com.bookshop.sachservice.controller;

import com.bookshop.sachservice.constants.SachConstants;
import com.bookshop.sachservice.dto.ResponseDto;
import com.bookshop.sachservice.dto.ResponsePayload;
import com.bookshop.sachservice.dto.SachDto;
import com.bookshop.sachservice.dto.TrangThaiSach;
import com.bookshop.sachservice.exception.SachNotFoundException;
import com.bookshop.sachservice.model.Sach;
import com.bookshop.sachservice.service.impl.SachServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
        return createResponse(request, SachConstants.MESSAGE_CREATED, HttpStatus.CREATED, payload);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(ResponseDto.<SachDto>builder()
//                        .apiPath(request.getDescription(false))
//                        .message(SachConstants.MESSAGE_CREATED)
//                        .timestamp(LocalDateTime.now())
//                        .statusCode(HttpStatus.CREATED)
//                        .payload(payload)
//                        .build()
//                );
    }

    @GetMapping("all/{role}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseDto<SachDto>> getAllProducts(@RequestParam("page") Optional<Integer> os,
                                                               @RequestParam("pageSize") Optional<Integer> ps,
                                                               @RequestParam("ten") Optional<String> ts,
                                                               @RequestParam("loai") Optional<String> tl,
                                                               @RequestParam("gia") Optional<BigDecimal> g,
                                                               @RequestParam("direction") Optional<String> d,
                                                               @RequestParam("sortBy") Optional<String> sb,
                                                               @PathVariable("role") String role,
                                                               WebRequest request) {
        int page = os.orElse(0);
        int pageSize = ps.orElse(8);
        String tenSach = ts.orElse("");
        String tenLoai = tl.orElse("");
        BigDecimal gia = g.orElse(BigDecimal.valueOf(Integer.MAX_VALUE));
        String direction = d.orElse("asc");
        String sortBy = sb.orElse("id");
        System.out.println(sortBy);
        Sort sort = generateSortPolicy(sortBy, direction);
        System.out.println(gia);
        Page<SachDto> sachDtoPage = null;
        if (role.equals("admin")) {
            sachDtoPage = crudService.findWithConditionAdmin(page, pageSize, tenSach, tenLoai, gia, sort);
        } else {
            sachDtoPage = crudService.findWithConditionUser(page, pageSize, tenSach, tenLoai, gia, sort);
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
        return createResponse(request, SachConstants.MESSAGE_OK, HttpStatus.OK, payload);
    }

    @GetMapping("{sachId}")
    public ResponseEntity<ResponseDto<SachDto>> getSach(@PathVariable Long sachId, WebRequest webRequest) {
        SachDto sachDto = crudService.getSach(sachId);
        ResponsePayload<SachDto> payload = ResponsePayload.<SachDto>builder()
                .records(List.of(sachDto))
                .recordCounts(1L).pageSize(1).currentPageSize(1).currentPage(0).totalPages(1)
                .build();
        return createResponse(webRequest, "OK", HttpStatus.OK, payload);
    }

    @GetMapping("random")
    public ResponseEntity<ResponseDto<SachDto>> getRandomSach(WebRequest request) {
       List<SachDto> sachDtos = crudService.getRandomSach();
       if(Objects.isNull(sachDtos) || sachDtos.isEmpty()) {
           System.out.println(sachDtos);
           throw new SachNotFoundException("Có lỗi xảy ra. Vui lòng thử lại sau");
       }
       ResponsePayload<SachDto> payload = ResponsePayload.<SachDto>builder()
                .records(sachDtos)
                .recordCounts((long)sachDtos.size())
                .currentPage(0)
                .currentPageSize(sachDtos.size())
                .totalPages(1)
                .pageSize(sachDtos.size())
                .build();
       return createResponse(request, SachConstants.MESSAGE_OK, HttpStatus.OK, payload);
    }

    @PutMapping
    public ResponseEntity<ResponseDto<SachDto>> updateSach(@Valid @RequestBody SachDto sachDto, WebRequest request) {
        List<SachDto> createdSachDtos = crudService.update(sachDto, sachDto.getTrangThai());
        ResponsePayload<SachDto> payload = ResponsePayload.<SachDto>
                        builder()
                .records(createdSachDtos).recordCounts((long) createdSachDtos.size())
                .currentPage(0).currentPageSize(1).totalPages(1).pageSize(1)
                .build();
        return createResponse(request, SachConstants.MESSAGE_OK, HttpStatus.OK, payload);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<SachDto>> markSachAsDeleted(@RequestBody SachDto sachDto, WebRequest request) {
        crudService.delete(sachDto);
        return createResponse(request, SachConstants.MESSAGE_OK, HttpStatus.OK, null);
    }

//    Internal API
    @GetMapping("trangThaiGia")
    public List<TrangThaiSach> getTrangThaiGia(@RequestParam List<Long> sachIds) {
        return crudService.getTrangThaiGia(sachIds)
                .stream()
                .sorted(Comparator.comparingLong(Sach::getId))
                .map(sach -> new TrangThaiSach(sach.getId(), sach.getGiaSach().getGiaBan(), sach.getTrangThai()))
                .toList();
    }

    private ResponseEntity<ResponseDto<SachDto>> createResponse(WebRequest request, String message, HttpStatus status, ResponsePayload<SachDto> payload) {
        return ResponseEntity
                .status(status)
                .body(ResponseDto.<SachDto>builder()
                        .apiPath(request.getDescription(false))
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .statusCode(status)
                        .payload(payload)
                        .build()
                );
    }

    private Sort generateSortPolicy(String sortBy, String directionString) {
        directionString = directionString.equals("desc") ? "desc" : "asc";
        Sort.Direction direction = Sort.Direction.fromString(directionString);
        return switch (sortBy) {
            case "gia" -> Sort.by(direction, "giaSach_giaBan");
            case "soLuong" -> Sort.by(direction, "soLuong");
            case "trangThai" -> Sort.by(direction, "trangThai");
            default -> Sort.by(direction, "ten");
        };
    }
    
    @GetMapping("id")
    public Long getIdSach(@RequestParam Long sachId){
        return crudService.getIdSach(sachId);
    }
}