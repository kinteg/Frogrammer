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
    private SimpleUserDto user;
    private Set<SimpleTagDto> tags;

    public static SimplePostDto toSimplePost(Post post) {
        post.setTags(post.getTags().stream().filter(tag -> tag.getId() > 0).collect(Collectors.toSet()));
        return SimplePostDto
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .mainText(post.getMainText())
                .preview(post.getPreview())
                .user(SimpleUserDto.toSimpleUser(post.getUser()))
                .tags(post.getTags()
                        .stream()
                        .map(SimpleTagDto::toSimpleTag)
                        .collect(Collectors.toSet())
                )
                .build();
    }

}
