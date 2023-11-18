package com.bookshop.sachservice.repository;

import com.bookshop.sachservice.model.Sach;
import org.bson.types.Decimal128;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SachRepository extends MongoRepository<Sach, Long> {

    Optional<Sach> findByTenAndTacGia(String s, String tg);
    @Query("{'$and':  [{'ten': {'$regex': ?0, '$options': 'i'}}, " +
            "{'loai.ten': {'$regex': ?1, '$options': 'i'}}, " +
            "{'$expr': {$lte: [{$subtract: ['$giaSach.giaBan', {'$multiply': ['$giaSach.giaBan', '$giaSach.phanTramGiam']}]}, ?2]}}]}")
    Page<Sach> findWithOptionalConditionAdmin(String tenSach, String tenLoai, Decimal128 gia, Pageable pageable);
    @Query("{'$and':  [{'ten': {'$regex': ?0, '$options': 'i'}}, " +
            "{'loai.ma': {'$regex': ?1, '$options': 'i'}}, " +
            "{'$expr': {$lte: [{$subtract: ['$giaSach.giaBan', {'$multiply': ['$giaSach.giaBan', '$giaSach.phanTramGiam']}]}, ?2]}}," +
            "{'trangThai': true}]}")
    Page<Sach> findWithOptionalConditionUser(String tenSach, String tenLoai, Decimal128 gia, Pageable pageable);

//    Page<Sach> findAllByTenLikeAndLoai_TenLikeAndGiaSach_GiaBan
    List<Sach> findAllByLoai_MaIn(List<String> tenLoai);

    List<Sach> findAllByIdOrderById(Iterable<Integer> saches);
}
