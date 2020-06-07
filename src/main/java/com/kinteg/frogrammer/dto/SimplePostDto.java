package com.kinteg.frogrammer.dto;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder(toBuilder = true)
public class SimplePostDto {

    private Long id;
    private String title;
    private String mainText;
    private String preview;
    private String username;
    private Set<Tag> tags;

    public static SimplePostDto toSimplePost(Post post) {
        return SimplePostDto
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .mainText(post.getMainText())
                .preview(post.getPreview())
                .username(post.getUser().getUsername())
                .tags(post.getTags())
                .build();
    }

}
