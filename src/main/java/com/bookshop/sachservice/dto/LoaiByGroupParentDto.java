package com.bookshop.sachservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoaiByGroupParentDto {
    private String parent;
    private List<LoaiByGroupChildDto> loais;

//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder()
//    }

    @Override
    public boolean equals(Object obj) {

        LoaiByGroupParentDto other = (LoaiByGroupParentDto) obj;
        return this.parent.equals(other.parent);
    }
}
