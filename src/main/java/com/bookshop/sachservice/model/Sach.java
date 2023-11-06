package com.bookshop.sachservice.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;


@Document(collection = "sach")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter @ToString
public class Sach {

    @Transient
    public static final String SEQUENCE_NAME = "sachSeq";

    @Id
    private Long id;
    private String ten;
    private String moTa;
    private LocalDate nxb;
    private String tacGia;
    private String anh;
    private Boolean trangThai;
    private GiaSach giaSach;
    private Integer soLuong;

//    @Field("loai")
//    @DocumentReference(lookup = "{ 'ma' : ?#{#self.ma} }")
    private Loai loai;
}
