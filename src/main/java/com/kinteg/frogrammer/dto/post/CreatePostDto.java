package com.kinteg.frogrammer.dto.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
public class CreatePostDto {

    @NotNull(message = "Require title isn't null.")
    @NotEmpty(message = "Pleas provide a title.")
    private String title;


    @NotNull(message = "Require mainText isn't null.")
    @NotEmpty(message = "Pleas provide a mainText.")
    private String mainText;


    @NotNull(message = "Require preview isn't null.")
    @NotEmpty(message = "Pleas provide a preview.")
    private String preview;

    @NotNull(message = "Require tags isn't null.")
    @NotEmpty(message = "Pleas provide tags.")
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
