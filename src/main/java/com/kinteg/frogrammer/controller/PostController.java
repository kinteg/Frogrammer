package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
    public ResponseEntity<SimplePostDto> getPost(@NotNull @Min(1) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(postService.getPost(id));
        } catch (HttpClientErrorException ex) {
            return new ResponseEntity<>(ex.getStatusCode());
        }
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Page<SimplePostDto> getAll(@PageableDefault(sort = "id") Pageable pageable) {
        return postService.getAll(pageable);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteById(@NotNull @Min(1) @PathVariable Long id) {
        try {
            postService.delete(id);
            return ResponseEntity.ok("");
        } catch (HttpClientErrorException ex) {
            return new ResponseEntity<>(ex.getStatusCode());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public ResponseEntity<SimplePostDto> update(@Valid @RequestBody CreatePostDto post, @Min(1) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(postService.update(post, id));
        } catch (HttpClientErrorException ex) {
            return new ResponseEntity<>(ex.getStatusCode());
        }
    }

    @PostMapping(value = "/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public SimplePostDto create(@Valid @RequestBody CreatePostDto post) {
        return postService.create(post);
    }

}
