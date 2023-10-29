package com.bookshop.sachservice.service;

import com.bookshop.sachservice.dto.SachDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICrudService<T> {

    List<T> create(T dto);

    List<T> findAll();

    List<T> update(T dto, boolean delete);

    void delete(T dto);
}
