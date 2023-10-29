package com.bookshop.sachservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
@Document
@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GiaSach {
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal giaGoc;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal giaBan;
    private LocalDate startTime;
    private LocalDate endTime;
    private Double phanTramGiam;
}
