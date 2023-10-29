package com.bookshop.sachservice.repository;

import com.bookshop.sachservice.model.Loai;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoaiRepository extends MongoRepository<Loai, ObjectId> {
}
