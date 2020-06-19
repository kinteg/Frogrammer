package com.kinteg.frogrammer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kinteg.frogrammer.db.domain.Post;
import com.kinteg.frogrammer.db.domain.Views;
import com.kinteg.frogrammer.dto.CreatePostDto;
import com.kinteg.frogrammer.dto.PostPageDto;
import com.kinteg.frogrammer.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    @Cacheable(value = "getPost", key = "#id")
    @JsonView(Views.FullPost.class)
    public ResponseEntity<Post> getPost(@NotNull @Min(1) @PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.HEAD)
    @Cacheable(value = "existPost", key = "#id")
    public ResponseEntity<?> head(@PathVariable Long id) {
        postService.getPost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    @Cacheable(value = "getAll", key = "#pageable")
    @JsonView(Views.FullPost.class)
    public PostPageDto getAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                      Pageable pageable) {
        return postService.getAll(pageable);
    }

    @DeleteMapping(value = "/{id}")
    @CacheEvict(value = {"getAll", "existPost", "getPost"}, key = "#id")
    public ResponseEntity<?> deleteById(@NotNull @Min(1) @PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    @Caching(
            put = {
                    @CachePut(value = {"existPost", "getPost"}, key = "#id")
            },
            evict = {
                    @CacheEvict(value = "getAll")
            }
    )
    @JsonView(Views.FullPost.class)
    public ResponseEntity<Post> update(
            @Valid @RequestBody CreatePostDto createPostDto,
            @Min(1) @PathVariable Long id) {

        Post post = postService.update(createPostDto, id);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(uri).body(post);
    }

    @PostMapping(value = "/create", consumes = "application/json")
    @Caching(
            evict = {
                    @CacheEvict(value = "getAll")
            }
    )
    @JsonView(Views.FullPost.class)
    public ResponseEntity<Post> create(@Valid @RequestBody CreatePostDto createPostDto) {
        Post post = postService.create(createPostDto);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(uri).body(post);
    }

}
