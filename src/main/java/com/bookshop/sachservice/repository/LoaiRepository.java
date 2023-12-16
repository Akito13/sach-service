package com.bookshop.sachservice.repository;

import com.bookshop.sachservice.model.Loai;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoaiRepository extends MongoRepository<Loai, ObjectId> {
    @Aggregation(pipeline = {"{'$sample': {'size': 4}}"})
    AggregationResults<Loai> random();

    List<Loai> findAllByMa(String ma);
    List<Loai> findAllByTen(String ten);
}
