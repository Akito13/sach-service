package com.bookshop.sachservice.service.impl;

import com.bookshop.sachservice.dto.LoaiByGroupChildDto;
import com.bookshop.sachservice.dto.LoaiByGroupParentDto;
import com.bookshop.sachservice.dto.LoaiDto;
import com.bookshop.sachservice.exception.LoaiAlreadyExistException;
import com.bookshop.sachservice.mapper.CommonMapper;
import com.bookshop.sachservice.model.Loai;
import com.bookshop.sachservice.repository.LoaiRepository;
import com.bookshop.sachservice.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class LoaiGroupServiceImpl {

    private LoaiRepository loaiRepository;

    public void create(LoaiDto loaiDto) {
        List<Loai> loaisByMa = loaiRepository.findAllByMa(loaiDto.getMa());
        if(!loaisByMa.isEmpty()) {
            throw new LoaiAlreadyExistException("Mã đã tồn tại");
        }
        List<Loai> loaisByTen = loaiRepository.findAllByTen(loaiDto.getTen());
        if(!loaisByTen.isEmpty()) {
            throw new LoaiAlreadyExistException("Tên đã tồn tại");
        }
        loaiRepository.save(CommonMapper.mapToLoai(loaiDto));
    }

    public List<LoaiDto> findAllWithougGrouping() {
        List<Loai> loaiList = loaiRepository.findAll();
        return loaiList.stream().map(loai -> CommonMapper.mapToLoaiDto(loai))
                .toList();
    }
    private List<LoaiByGroupParentDto> create(LoaiByGroupParentDto dto) {
        return null;
    }

    private Set<String> getParentSet(List<Loai> loaiList) {
        Set<String> parentName = new HashSet<>();
        loaiList.stream().forEach(loai ->  {
            String parent = loai.getParent();
            parentName.add(parent);
        });
        return parentName;
    }

    public Set<LoaiByGroupParentDto> findAll() {
        List<Loai> loaiList = loaiRepository.findAll();

        Set<LoaiByGroupParentDto> loaiParentSet = new HashSet<>();

        Set<String> parentName = getParentSet(loaiList);

        parentName.forEach(s -> {
            LoaiByGroupParentDto loaiByGroupParentDto = new LoaiByGroupParentDto();
            loaiByGroupParentDto.setParent(s);
            loaiParentSet.add(loaiByGroupParentDto);
        });

        loaiParentSet.forEach(loaiParent -> {
            loaiParent.setLoais(new ArrayList<>());
            List<LoaiByGroupChildDto> childLoaiList = loaiParent.getLoais();
            for(int i = 0;i < loaiList.size(); i++) {
                if(loaiParent.getParent().equals(loaiList.get(i).getParent())) {
                    Loai loai = loaiList.get(i);
                    LoaiByGroupChildDto loaiByGroupChildDto = new LoaiByGroupChildDto();
                    loaiByGroupChildDto.setMa(loai.getMa());
                    loaiByGroupChildDto.setTen(loai.getTen());
                    childLoaiList.add(loaiByGroupChildDto);
                }
            }
        });
        loaiParentSet.forEach(loaiByGroupParentDto -> {
            System.out.println(loaiByGroupParentDto);
            System.out.println(loaiByGroupParentDto.getLoais().size());
            loaiByGroupParentDto.getLoais().forEach(loaiByGroupChildDto -> System.out.println(loaiByGroupChildDto.getTen()));
        });
        return loaiParentSet;
    }

    public Set<String> findParents() {
        List<Loai> allLoai = loaiRepository.findAll();
        return getParentSet(allLoai);
    }
}
