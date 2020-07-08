package com.kinteg.frogrammer.dto.tag;

import com.kinteg.frogrammer.db.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class TagPageDto {

    private List<SimpleTagDto> posts;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;

    public static TagPageDto toSimpleTag(Page<Tag> tags) {
        return TagPageDto
                .builder()
                .posts(tags.getContent()
                        .stream().map(SimpleTagDto::toSimpleTag)
                        .collect(Collectors.toList()))
                .currentPage(tags.getPageable().getPageNumber())
                .totalPages(tags.getTotalPages())
                .totalElements(tags.getTotalElements())
                .size(tags.getSize())
                .build();
    }

}
