package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/post")
@Slf4j
@Validated
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public SimplePostDto getPost(@NotNull @Min(1) @PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Page<SimplePostDto> getAll(@PageableDefault(sort = "id") Pageable pageable) {
        return postService.getAll(pageable);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteById(@NotNull @Min(1) @PathVariable Long id) {
        postService.delete(id);
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public SimplePostDto update(@Valid @RequestBody CreatePostDto post, @Min(1) @PathVariable Long id) {
        return postService.update(post, id);
    }

    @PostMapping(value = "/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public SimplePostDto create(@Valid @RequestBody CreatePostDto post) {
        return postService.create(post);
    }

}
