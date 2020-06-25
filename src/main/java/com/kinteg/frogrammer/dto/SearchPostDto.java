package com.kinteg.frogrammer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class SearchPostDto {

    private String title;
    private String mainText;
    private String preview;
    private List<Long> tags;

    public SearchPostDto fix() {
        title = Objects.requireNonNullElse(title, "");
        mainText = Objects.requireNonNullElse(mainText, "");
        preview = Objects.requireNonNullElse(preview, "");
        tags = Objects.requireNonNullElse(tags, Collections.singletonList(0L));

        if (tags.size() == 0) {
            tags.add(0L);
        }

        return this;
    }

}
