package com.kinteg.frogrammer.service.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public interface PostService {

    SimplePostDto getPost(Long id) throws HttpClientErrorException;

    Page<SimplePostDto> getAll(Pageable pageable);

    SimplePostDto create(CreatePostDto createPostDto);

    SimplePostDto update(CreatePostDto createPostDto, Long id) throws HttpClientErrorException;

    void delete(Long id) throws HttpClientErrorException;

}
