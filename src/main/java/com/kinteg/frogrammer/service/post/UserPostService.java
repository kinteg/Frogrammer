package com.kinteg.frogrammer.service.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.post.CreatePostDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public interface UserPostService {

    Post create(CreatePostDto createPostDto);

    Post update(CreatePostDto createPostDto, Long id) throws HttpClientErrorException;

    void delete(Long id) throws HttpClientErrorException;

}
