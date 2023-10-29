package com.bookshop.sachservice.service;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface IPageCrudService<T> {
    Page<T> findAll(int offset, int pageSize);
    Page<T> findWithConditionAdmin(int offset, int pageSize, String tenSach, String tenLoai, BigDecimal gia);
    Page<T> findWithConditionUser(int offset, int pageSize, String tenSach, String tenLoai, BigDecimal gia);
}
