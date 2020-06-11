package com.kinteg.frogrammer.dto;

import com.kinteg.frogrammer.db.domain.Post;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
public class SimplePostDto {

    private Long id;
    private String title;
    private String mainText;
    private String preview;
    private String username;
    private Set<SimpleTagDto> tags;

    public static SimplePostDto toSimplePost(Post post) {
        return SimplePostDto
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .mainText(post.getMainText())
                .preview(post.getPreview())
                .username(post.getUser().getUsername())
                .tags(post.getTags()
                        .stream()
                        .map(SimpleTagDto::toSimpleTag)
                        .collect(Collectors.toSet())
                )
                .build();
    }

}
