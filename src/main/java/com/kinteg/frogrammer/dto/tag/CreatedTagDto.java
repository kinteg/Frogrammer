package com.kinteg.frogrammer.dto.tag;

import com.kinteg.frogrammer.db.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreatedTagDto {

    @NotNull(message = "Require title isn't null.")
    @NotEmpty(message = "Pleas provide a title.")
    private String title;

    @NotNull(message = "Require description isn't null.")
    @NotEmpty(message = "Pleas provide a description.")
    private String description;

    public Tag toTag() {
        return Tag
                .builder()
                .title(title)
                .description(description)
                .build();

    }

}
