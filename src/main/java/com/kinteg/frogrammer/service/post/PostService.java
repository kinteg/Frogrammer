package com.kinteg.frogrammer.service.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    SimplePostDto getPost(Long id);

    Page<SimplePostDto> getAll(Pageable pageable);

    SimplePostDto create(CreatePostDto createPostDto);

    SimplePostDto update(CreatePostDto createPostDto, Long id);

    void delete(Long id);

}
