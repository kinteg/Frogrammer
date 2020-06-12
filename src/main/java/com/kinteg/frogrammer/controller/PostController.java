package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.SimplePostDto;
import com.kinteg.frogrammer.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("api/post")
@Slf4j
@Validated
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.POST,
                        HttpMethod.PUT, HttpMethod.DELETE,
                        HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getPost(@NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.HEAD)
    public ResponseEntity<?> head(@PathVariable Long id) {
        postService.getPost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Page<SimplePostDto> getAll(@PageableDefault(sort = "id") Pageable pageable) {
        return postService.getAll(pageable);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@NotNull @Min(1) @PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public ResponseEntity<Object> update(
            @Valid @RequestBody CreatePostDto createPostDto,
            @Min(1) @PathVariable Long id) {

        SimplePostDto post = postService.update(createPostDto, id);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(uri).body(post);
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<SimplePostDto> create(@Valid @RequestBody CreatePostDto createPostDto) {
        SimplePostDto post = postService.create(createPostDto);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(uri).body(post);
    }

}
