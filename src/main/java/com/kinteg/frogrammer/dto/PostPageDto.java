package com.kinteg.frogrammer.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonView(Views.FullPost.class)
public class PostPageDto {

    private List<Post> messages;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;

}
