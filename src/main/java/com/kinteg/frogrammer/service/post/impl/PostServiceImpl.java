package com.kinteg.frogrammer.service.post.impl;

import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.repository.PostRepo;
import com.kinteg.frogrammer.dto.post.SearchPostDto;
import com.kinteg.frogrammer.service.post.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;

    public PostServiceImpl(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    @Override
    public Post getPost(Long id) throws HttpClientErrorException {
        return postRepo.getByIdAndStatus(id, Status.ACTIVE.name())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Failed post id."));
    }

    @Override
    public Page<Post> searchAll(Pageable pageable) {
        return postRepo.findAllByStatus(pageable, Status.ACTIVE.name());
    }

    @Override
    public Page<Post> deepSearch(Pageable pageable, SearchPostDto searchPost) {
        return postRepo.findAllBySearchPostDto(
                pageable, searchPost.fix(), Status.ACTIVE.name());
    }

    @Override
    public Page<Post> searchByTag(Pageable pageable, List<Long> tags) {
        tags.add(0L);
        return postRepo.findAllByTags(
                pageable, tags, Status.ACTIVE.name());
    }

    @Override
    public Page<Post> searchByText(Pageable pageable, String searchText) {
        return postRepo.findAllByText(
                pageable, Objects.toString(searchText, ""), Status.ACTIVE.name());
    }

}