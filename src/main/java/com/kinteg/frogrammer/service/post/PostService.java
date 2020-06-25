package com.kinteg.frogrammer.service.post;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.PostPageDto;
import com.kinteg.frogrammer.dto.SearchPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public interface PostService {

    Post getPost(Long id) throws HttpClientErrorException;

    Page<Post> getAll(Pageable pageable);

    Post create(CreatePostDto createPostDto);

    Post update(CreatePostDto createPostDto, Long id) throws HttpClientErrorException;

    void delete(Long id) throws HttpClientErrorException;

    Page<Post> search(Pageable pageable, SearchPostDto searchPost);

    Page<Post> searchByTag(Pageable pageable, List<Long> tags);

    Page<Post> searchByText(Pageable pageable, String searchText);

}
