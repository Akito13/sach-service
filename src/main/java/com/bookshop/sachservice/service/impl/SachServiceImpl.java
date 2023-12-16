package com.bookshop.sachservice.service.impl;

import com.bookshop.sachservice.constants.AwsConstants;
import com.bookshop.sachservice.dto.*;
import com.bookshop.sachservice.exception.InvalidBodyException;
import com.bookshop.sachservice.exception.SachAlreadyExistsException;
import com.bookshop.sachservice.exception.SachNotFoundException;
import com.bookshop.sachservice.mapper.CommonMapper;
import com.bookshop.sachservice.model.Loai;
import com.bookshop.sachservice.model.Sach;
import com.bookshop.sachservice.repository.LoaiRepository;
import com.bookshop.sachservice.repository.SachRepository;
import com.bookshop.sachservice.service.FileStoreService;
import com.bookshop.sachservice.service.ICrudService;
import com.bookshop.sachservice.service.IPageCrudService;
import com.bookshop.sachservice.service.NextSequenceSerice;
import lombok.AllArgsConstructor;
import org.apache.http.entity.ContentType;
import org.bson.types.Decimal128;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;

@Service("sachService")
@AllArgsConstructor
public class SachServiceImpl implements ICrudService<SachDto, Long>, IPageCrudService<SachDto> {

    private SachRepository sachRepository;
    private NextSequenceSerice sequenceService;
    private LoaiRepository loaiRepository;
    private FileStoreService fileStoreService;
    private final String awsS3Path = "https://" + AwsConstants.S3_BUCKET_NAME + ".s3." + AwsConstants.S3_REGION +".amazonaws.com";

    public Long create(SachDtoWithFile sachDtoWithFile) {
        SachDto sachDto = SachDto.builder()
                .giaSach(GiaDto.builder()
                        .giaGoc(sachDtoWithFile.getGiaGoc())
                        .giaBan(sachDtoWithFile.getGiaBan())
                        .thoiGian(new TimeRange(sachDtoWithFile.getStartTime(), sachDtoWithFile.getEndTime()))
                        .phanTramGiam(sachDtoWithFile.getPhanTramGiam())
                        .build())
                .moTa(sachDtoWithFile.getMoTa())
                .loaiDto(LoaiDto.builder()
                        .ten(sachDtoWithFile.getTenLoai())
                        .ma(sachDtoWithFile.getMaLoai())
                        .parent(sachDtoWithFile.getParentLoai())
                        .build())
                .soLuong(sachDtoWithFile.getSoLuong())
                .anh(sachDtoWithFile.getAnh())
                .ten(sachDtoWithFile.getTen())
                .tacGia(sachDtoWithFile.getTacGia())
                .nxb(sachDtoWithFile.getNxb())
                .build();
        return create(sachDto, sachDtoWithFile.getImg());
    }
    @Override
    public Long create(SachDto sachDto, MultipartFile file) {
        if(file.isEmpty()) {
            throw new InvalidBodyException("Chưa có file hình ảnh");
        }

        if(!Arrays.asList(ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType(),
                ContentType.IMAGE_BMP.getMimeType(),
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_WEBP.getMimeType()).contains(file.getContentType())) {
            throw new InvalidBodyException("File phải là định dạng ảnh");
        }
        System.out.println(file.getName());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        sachDto.setTrangThai(true);
        Optional<Sach> optionalSach = sachRepository.findByTenAndTacGia(sachDto.getTen(), sachDto.getTacGia());
        if (optionalSach.isPresent()) {
            throw new SachAlreadyExistsException("Sách này đã tồn tại");
        }
        Sach sach = CommonMapper.mapToSach(sachDto, sachDto.getLoaiDto(), sachDto.getGiaSach());

        try {
            fileStoreService.upload(AwsConstants.S3_BUCKET_NAME + "/", sach.getAnh(), Optional.of(metadata), file.getInputStream());
            sach.setId(sequenceService.getNextSequence(Sach.SEQUENCE_NAME));
            sach.setAnh(awsS3Path + "/" + sach.getAnh());
            Sach savedSach = sachRepository.save(sach);
            return savedSach.getId();
        } catch (Exception e) {
            throw new IllegalStateException("Thêm thất bại", e);
        }
//        sach.setId(sequenceService.getNextSequence(Sach.SEQUENCE_NAME));
//        Sach savedSach = sachRepository.save(sach);
//        return savedSach.getId();

//        try {
//            System.out.println(sach.getId() + ":"
//                    + sach.getTen() + ":"
//                    + sach.getGiaSach() + ":"
//                    + sach.getGiaSach().getGiaBan() + ":"
//                    + sach.getSoLuong() + ":"
//                    + sach.getNxb() + ":"
//                    + sach.getTacGia()+":"
//                    + sach.getLoai().getMa()+":"
//                    + sach.getLoai().getTen()+":"
//                    + sach.getLoai().getParent()+":"
//                    + sach.getAnh());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public List<SachDto> findAll() {
        return sachRepository.findAll().stream().map(sach -> CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach())).toList();
    }
    @Override
    public Page<SachDto> findWithConditionAdmin(int offset, int pageSize, String tenSach, String tenLoai, BigDecimal gia, Sort sort) {
        return sachRepository.findWithOptionalConditionAdmin(tenSach, tenLoai, new Decimal128(gia.round(MathContext.DECIMAL128)),PageRequest.of(offset, pageSize, sort))
                .map(sach -> CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach()));
    }

    @Override
    public Page<SachDto> findWithConditionUser(int offset, int pageSize, String tenSach, String tenLoai, BigDecimal gia, Sort sort) {
        return sachRepository.findWithOptionalConditionUser(tenSach, tenLoai, new Decimal128(gia.round(MathContext.DECIMAL128)),PageRequest.of(offset, pageSize, sort))
                .map(sach -> CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach()));
    }

    @Override
    public List<SachDto> update(SachDto sachDto, boolean trangThai) {
        sachDto.setTrangThai(trangThai);
        Optional<Sach> optionalSach = sachRepository.findById(sachDto.getId());
        if (optionalSach.isEmpty()) {
            throw new SachNotFoundException("Sách không tồn tại");
        }
        Sach foundSach = optionalSach.get();
        Long id = foundSach.getId();
        foundSach = CommonMapper.mapToSach(sachDto, sachDto.getLoaiDto(), sachDto.getGiaSach());
        foundSach.setId(id);
        try {
            System.out.println(foundSach.getId() + ":"
                    + foundSach.getTen() + ":"
                    + foundSach.getGiaSach() + ":"
                    + foundSach.getGiaSach().getGiaBan() + ":"
                    + foundSach.getSoLuong() + ":"
                    + foundSach.getNxb() + ":"
                    + foundSach.getTacGia()+":"
                    + foundSach.getLoai().getMa()+":"
                    + foundSach.getLoai().getTen()+":"
                    + foundSach.getLoai().getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sach savedSach = sachRepository.save(foundSach);
        return Collections.singletonList(CommonMapper.mapToSachDto(savedSach, savedSach.getLoai(), savedSach.getGiaSach()));
    }

    @Override
    public void delete(Long sachId) {
        Optional<Sach> result = sachRepository.findById(sachId);
        if(result.isPresent()){
            Sach sach = result.get();
            sach.setTrangThai(false);
            sachRepository.save(sach);
        }
    }

    public void recoverSach(Long sachId) {
        Optional<Sach> result = sachRepository.findById(sachId);
        if(result.isPresent()) {
            Sach sach = result.get();
            sach.setTrangThai(true);
            sachRepository.save(sach);
        }
    }

    public List<SachDto> getRandomSach() {
        List<Loai> loais = loaiRepository.random().getMappedResults();
        List<String> maLoais = loais.stream().map(Loai::getMa).toList();
        maLoais.forEach(s -> System.out.println("Loại random là: " + s));
        return sachRepository.findAllByLoai_MaIn(maLoais).stream()
                 .map(sach -> CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach()))
                 .toList();
    }

    @Override
    @Deprecated
    public Page<SachDto> findAll(int offset, int pageSize) {
        return null;
    }

    public SachDto getSach(Long sachId) {
       Optional<Sach> result = sachRepository.findById(sachId);
       if(result.isEmpty()) {
           throw new SachNotFoundException("Sách không có");
       }
       Sach sach = result.get();
       return CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach());
    }

//    Internal API
    public List<Sach> getTrangThaiGia(List<Long> sachIds){
        return sachRepository.findAllById(sachIds);
    }
//    Internal API
    public Long getIdSach(Long id) {
        Optional<Sach> optionalSach = sachRepository.findById(id);
        return optionalSach.map(Sach::getId).orElse(-1L);
    }
    private boolean checkNgayGiamGia(LocalDate endTime){
        return endTime.isAfter(LocalDate.now());
    }
}
