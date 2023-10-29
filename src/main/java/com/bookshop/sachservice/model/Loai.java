package com.bookshop.sachservice.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "loai")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter @ToString
public class Loai {
    private String ma;

    private String ten;

    private String parent;

//    @ReadOnlyProperty
//    @DocumentReference(lookup = "{'loai':?#(#self.ma)}")
    private List<Sach> sachs;
}
