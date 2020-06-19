package com.kinteg.frogrammer.service.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.PostPageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public interface PostService {

    Post getPost(Long id) throws HttpClientErrorException;

    PostPageDto getAll(Pageable pageable);

    Post create(CreatePostDto createPostDto);

    Post update(CreatePostDto createPostDto, Long id) throws HttpClientErrorException;

    void delete(Long id) throws HttpClientErrorException;

}
