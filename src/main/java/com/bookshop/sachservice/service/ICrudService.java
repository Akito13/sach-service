package com.bookshop.sachservice.service;

import com.bookshop.sachservice.dto.SachDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICrudService<T, Id> {

    Id create(T dto, MultipartFile file);

    List<T> findAll();

    List<T> update(T dto, boolean delete);

    void delete(Long dto);
}
