package com.kinteg.frogrammer.dto.post;

import com.kinteg.frogrammer.db.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class PostPageDto {

    private List<SimplePostDto> posts;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;

    public static PostPageDto toSimplePost(Page<Post> posts) {
        return PostPageDto
                .builder()
                .posts(posts.getContent()
                        .stream().map(SimplePostDto::toSimplePost)
                        .collect(Collectors.toList()))
                .currentPage(posts.getPageable().getPageNumber())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .size(posts.getSize())
                .build();
    }

}
