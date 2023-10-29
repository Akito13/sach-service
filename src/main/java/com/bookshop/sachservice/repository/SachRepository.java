package com.bookshop.sachservice.repository;

import com.bookshop.sachservice.model.Sach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface SachRepository extends MongoRepository<Sach, String> {

    Optional<Sach> findByTenAndTacGia(String s, String tg);
    @Query("{'$and':  [{'ten': {'$regex': ?0, '$options': 'i'}}, " +
            "{'loai.ten': {'$regex': ?1, '$options': 'i'}}, " +
            "{'$expr': {$lte: [{$subtract: ['$giaSach.giaBan', {'$multiply': ['$giaSach.giaBan', '$giaSach.phanTramGiam']}]}, ?2]}}]}")
    Page<Sach> findWithOptionalConditionAdmin(String tenSach, String tenLoai, BigDecimal gia, Pageable pageable);
    @Query("{'$and':  [{'ten': {'$regex': ?0, '$options': 'i'}}, " +
            "{'loai.ten': {'$regex': ?1, '$options': 'i'}}, " +
            "{'$expr': {$lte: [{$subtract: ['$giaSach.giaBan', {'$multiply': ['$giaSach.giaBan', '$giaSach.phanTramGiam']}]}, ?2]}}," +
            "{'trangThai': true}]}")
    Page<Sach> findWithOptionalConditionUser(String tenSach, String tenLoai, BigDecimal gia, Pageable pageable);
}
