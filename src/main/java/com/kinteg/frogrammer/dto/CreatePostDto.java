package com.kinteg.frogrammer.dto;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class CreatePostDto {

    @NotNull
    @Size(min = 10)
    private String title;

    @NotNull
    @Size(min = 10)
    private String mainText;

    @NotNull
    @Size(min = 10)
    private String preview;

    @NotNull
    private Set<Long> tags;

    public Post toPost(Set<Tag> tags) {
        return Post
                .builder()
                .mainText(mainText)
                .title(title)
                .preview(preview)
                .tags(tags)
                .build();
    }



}
