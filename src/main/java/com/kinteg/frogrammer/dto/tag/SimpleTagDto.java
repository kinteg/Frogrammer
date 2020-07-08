package com.kinteg.frogrammer.dto.tag;

import com.kinteg.frogrammer.db.domain.Tag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SimpleTagDto {

    private Long id;
    private String title;
    private String description;

    public static SimpleTagDto toSimpleTag(Tag tag) {
        return SimpleTagDto
                .builder()
                .id(tag.getId())
                .title(tag.getTitle())
                .description(tag.getDescription())
                .build();
    }

}