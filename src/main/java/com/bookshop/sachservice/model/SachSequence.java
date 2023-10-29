package com.bookshop.sachservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = Sach.SEQUENCE_NAME)
@Getter
@Setter
public class SachSequence {
    @Id
    private String id;
    private int seq;
}
