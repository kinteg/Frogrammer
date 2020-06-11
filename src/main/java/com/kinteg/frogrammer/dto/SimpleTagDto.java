package com.kinteg.frogrammer.dto;

import com.kinteg.frogrammer.db.domain.Tag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SimpleTagDto {

    private Long id;
    private String title;

    public static SimpleTagDto toSimpleTag(Tag tag) {
        return SimpleTagDto
                .builder()
                .id(tag.getId())
                .title(tag.getTitle())
                .build();
    }

}
