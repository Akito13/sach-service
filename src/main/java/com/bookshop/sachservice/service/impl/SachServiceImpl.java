package com.bookshop.sachservice.service.impl;

import com.bookshop.sachservice.dto.SachDto;
import com.bookshop.sachservice.exception.SachAlreadyExistsException;
import com.bookshop.sachservice.exception.SachNotFoundException;
import com.bookshop.sachservice.mapper.CommonMapper;
import com.bookshop.sachservice.model.Sach;
import com.bookshop.sachservice.repository.SachRepository;
import com.bookshop.sachservice.service.ICrudService;
import com.bookshop.sachservice.service.IPageCrudService;
import com.bookshop.sachservice.service.NextSequenceSerice;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("sachService")
@AllArgsConstructor
public class SachServiceImpl implements ICrudService<SachDto>, IPageCrudService<SachDto> {

    private SachRepository sachRepository;
    private NextSequenceSerice sequenceService;
    @Override
    public List<SachDto> create(SachDto sachDto) {
        sachDto.setTrangThai(true);
        Optional<Sach> optionalSach = sachRepository.findByTenAndTacGia(sachDto.getTen(), sachDto.getTacGia());
        if (optionalSach.isPresent()) {
            throw new SachAlreadyExistsException("Sách này đã tồn tại");
        }
        Sach sach = CommonMapper.mapToSach(sachDto, sachDto.getLoaiDto(), sachDto.getGiaSach());
        sach.setId(sequenceService.getNextSequence(Sach.SEQUENCE_NAME));
        Sach savedSach = sachRepository.save(sach);
        return Collections.singletonList(CommonMapper.mapToSachDto(savedSach, savedSach.getLoai(), savedSach.getGiaSach()));
    }

    @Override
    public List<SachDto> findAll() {
        return sachRepository.findAll().stream().map(sach -> CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach())).toList();
    }
    @Override
    public Page<SachDto> findWithConditionAdmin(int offset, int pageSize, String tenSach, String tenLoai, BigDecimal gia) {
        return sachRepository.findWithOptionalConditionAdmin(tenSach, tenLoai, gia,PageRequest.of(offset, pageSize))
                .map(sach -> CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach()));
    }

    @Override
    public Page<SachDto> findWithConditionUser(int offset, int pageSize, String tenSach, String tenLoai, BigDecimal gia) {
        return sachRepository.findWithOptionalConditionUser(tenSach, tenLoai, gia,PageRequest.of(offset, pageSize))
                .map(sach -> CommonMapper.mapToSachDto(sach, sach.getLoai(), sach.getGiaSach()));
    }

    @Override
    public List<SachDto> update(SachDto sachDto, boolean delete) {
        sachDto.setTrangThai(delete);
        Optional<Sach> optionalSach = sachRepository.findByTenAndTacGia(sachDto.getTen(), sachDto.getTacGia());
        if (optionalSach.isEmpty()) {
            throw new SachNotFoundException("Sách không tồn tại");
        }
        Sach foundSach = optionalSach.get();
        int id = foundSach.getId();
        foundSach = CommonMapper.mapToSach(sachDto, sachDto.getLoaiDto(), sachDto.getGiaSach());
        foundSach.setId(id);
        sachRepository.save(foundSach);
        return Collections.singletonList(CommonMapper.mapToSachDto(foundSach, foundSach.getLoai(), foundSach.getGiaSach()));

    }

    @Override
    public void delete(SachDto sachDto) {
        if (sachDto.getTen() == null || sachDto.getTacGia() == null)
            throw new RuntimeException("Tên sách và tên tác giả chưa có");
        update(sachDto, true);
    }

    @Override
    @Deprecated
    public Page<SachDto> findAll(int offset, int pageSize) {
        return null;
    }

//    Internal API
    public List<Sach> getTrangThaiGia(List<Integer> sachIds){
        return sachRepository.findAllById(sachIds);
    }
//    Internal API
    public Integer getIdSach(Integer id) {
        Optional<Sach> optionalSach = sachRepository.findById(id);
        return optionalSach.map(Sach::getId).orElse(-1);
    }
    private boolean checkNgayGiamGia(LocalDate endTime){
        return endTime.isAfter(LocalDate.now());
    }
}
