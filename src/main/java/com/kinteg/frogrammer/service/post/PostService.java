package com.kinteg.frogrammer.service.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.post.SearchPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    Post getPost(Long id);

    Page<Post> searchAll(Pageable pageable);

    Page<Post> deepSearch(Pageable pageable, SearchPostDto searchPost);

    Page<Post> searchByTag(Pageable pageable, List<Long> tags);

    Page<Post> searchByText(Pageable pageable, String searchText);

}
