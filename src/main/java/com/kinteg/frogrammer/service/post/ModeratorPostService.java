package com.kinteg.frogrammer.service.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.post.CreatePostDto;
import com.kinteg.frogrammer.dto.post.SearchPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ModeratorPostService {

    Post getPostNotActive(Long id);

    Page<Post> searchAllNotActive(Pageable pageable);

    Page<Post> deepSearchNotActive(Pageable pageable, SearchPostDto searchPost);

    Page<Post> searchByTagNotActive(Pageable pageable, List<Long> tags);

    Page<Post> searchByTextNotActive(Pageable pageable, String searchText);

    void deletePost(Long id);

    void activatePost(Long id);

    void disablePost(Long id);

    Post create(CreatePostDto createPostDto);

    Post update(CreatePostDto createPostDto, Long id);

}
